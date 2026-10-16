package com.example.ui

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AivorynDatabase
import com.example.data.local.AppPreferences
import com.example.data.local.ConversationEntity
import com.example.data.local.MessageEntity
import com.example.data.local.ModelEntity
import com.example.data.local.PreferencesRepository
import com.example.data.repository.ChatRepository
import com.example.data.repository.ModelRepository
import com.example.inference.LlamaCppInferenceEngine
import com.example.inference.PromptFormatter
import com.example.ui.theme.ThemeMode
import com.example.util.ExtractedDocument
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    application: Application,
    val inferenceEngine: LlamaCppInferenceEngine
) : AndroidViewModel(application) {

    private val db = AivorynDatabase.getDatabase(application)
    val chatRepository = ChatRepository(db.conversationDao(), db.messageDao())
    val modelRepository = ModelRepository(db.modelDao())
    val preferencesRepository = PreferencesRepository(application)

    val preferences: StateFlow<AppPreferences> = preferencesRepository.preferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppPreferences())

    // Active model
    val activeModel: StateFlow<ModelEntity?> = modelRepository.activeModel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current conversation
    private val _currentConversationId = MutableStateFlow<String?>(null)
    val currentConversationId: StateFlow<String?> = _currentConversationId.asStateFlow()

    // Search query for history
    val searchQuery = MutableStateFlow("")

    val conversations: StateFlow<List<ConversationEntity>> = searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            chatRepository.activeConversations
        } else {
            chatRepository.searchConversations(query)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentMessages: StateFlow<List<MessageEntity>> = _currentConversationId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else chatRepository.getMessages(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Composer State
    val composerText = MutableStateFlow("")
    val isThinkMode = MutableStateFlow(false)
    val isSearchMode = MutableStateFlow(false)
    val attachedDocument = MutableStateFlow<ExtractedDocument?>(null)
    val isListening = MutableStateFlow(false)

    // Generation State
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _streamingMessage = MutableStateFlow<MessageEntity?>(null)
    val streamingMessage: StateFlow<MessageEntity?> = _streamingMessage.asStateFlow()

    private var generationJob: Job? = null

    fun selectConversation(id: String) {
        if (_isGenerating.value) stopGeneration()
        _currentConversationId.value = id
    }

    fun startNewChat() {
        if (_isGenerating.value) stopGeneration()
        _currentConversationId.value = null
        composerText.value = ""
        attachedDocument.value = null
    }

    fun toggleThinkMode() {
        isThinkMode.value = !isThinkMode.value
    }

    fun toggleSearchMode() {
        isSearchMode.value = !isSearchMode.value
    }

    fun attachDocument(doc: ExtractedDocument) {
        attachedDocument.value = doc
    }

    fun removeAttachment() {
        attachedDocument.value = null
    }

    fun onSpeechResult(text: String) {
        if (composerText.value.isBlank()) {
            composerText.value = text
        } else {
            composerText.value = "${composerText.value.trim()} $text"
        }
    }

    fun renameConversation(id: String, newTitle: String) {
        viewModelScope.launch {
            chatRepository.renameConversation(id, newTitle)
        }
    }

    fun togglePin(id: String) {
        viewModelScope.launch {
            chatRepository.togglePin(id)
        }
    }

    fun toggleArchive(id: String) {
        viewModelScope.launch {
            chatRepository.toggleArchive(id)
            if (_currentConversationId.value == id) {
                _currentConversationId.value = null
            }
        }
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            chatRepository.deleteConversation(id)
            if (_currentConversationId.value == id) {
                _currentConversationId.value = null
            }
        }
    }

    fun clearAllConversations() {
        viewModelScope.launch {
            chatRepository.deleteAllConversations()
            _currentConversationId.value = null
        }
    }

    fun sendMessage(textOverride: String? = null) {
        val query = (textOverride ?: composerText.value).trim()
        if (query.isBlank() && attachedDocument.value == null) return
        if (_isGenerating.value) return

        val doc = attachedDocument.value
        composerText.value = ""
        attachedDocument.value = null

        viewModelScope.launch {
            var convId = _currentConversationId.value
            if (convId == null) {
                val title = if (query.isNotBlank()) {
                    query.take(40).replace("\n", " ")
                } else {
                    doc?.fileName ?: "New Chat"
                }
                val newConv = chatRepository.createNewConversation(title, activeModel.value?.name ?: "")
                convId = newConv.id
                _currentConversationId.value = convId
            }

            val userMsg = MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = convId,
                role = "user",
                content = query,
                attachedFileName = doc?.fileName,
                attachedContentPreview = doc?.content?.take(5000),
                isSearchEnabled = isSearchMode.value
            )
            chatRepository.saveMessage(userMsg)

            // Trigger generation
            generateAssistantResponse(convId, userMsg)
        }
    }

    fun regenerate() {
        if (_isGenerating.value) return
        val convId = _currentConversationId.value ?: return

        viewModelScope.launch {
            val list = chatRepository.getMessagesList(convId)
            val lastUserMsg = list.lastOrNull { it.role == "user" } ?: return@launch
            generateAssistantResponse(convId, lastUserMsg)
        }
    }

    private fun generateAssistantResponse(convId: String, triggerMsg: MessageEntity) {
        generationJob?.cancel()
        _isGenerating.value = true

        val assistantMsgId = UUID.randomUUID().toString()
        val startTime = SystemClock.elapsedRealtime()

        val initialAssistantMsg = MessageEntity(
            id = assistantMsgId,
            conversationId = convId,
            role = "assistant",
            content = "",
            isSearchEnabled = isSearchMode.value
        )
        _streamingMessage.value = initialAssistantMsg

        generationJob = viewModelScope.launch {
            val model = activeModel.value
            val prefs = preferences.value
            val history = chatRepository.getMessagesList(convId)

            val formattedPrompt = PromptFormatter.formatConversation(
                architecture = model?.architecture ?: "Llama",
                messages = history,
                isThinkMode = isThinkMode.value,
                attachedContent = triggerMsg.attachedContentPreview
            )

            val accumulated = StringBuilder()
            var tokenCount = 0

            inferenceEngine.streamInference(
                prompt = formattedPrompt,
                temperature = prefs.temperature,
                topP = prefs.topP,
                maxTokens = prefs.contextSize
            )
                .catch { error ->
                    val errorText = "Error during inference: ${error.message}"
                    accumulated.append(errorText)
                    _streamingMessage.value = initialAssistantMsg.copy(content = accumulated.toString(), error = error.message)
                }
                .collect { chunk ->
                    accumulated.append(chunk)
                    tokenCount++

                    val (thinkPart, responsePart) = extractThinkBlock(accumulated.toString())
                    _streamingMessage.value = initialAssistantMsg.copy(
                        content = responsePart,
                        thinkContent = thinkPart,
                        isThinkExpanded = isThinkMode.value
                    )
                }

            val endTime = SystemClock.elapsedRealtime()
            val durationMs = (endTime - startTime).coerceAtLeast(1)
            val tps = if (tokenCount > 0) (tokenCount * 1000f) / durationMs else 0f

            val (finalThink, finalResponse) = extractThinkBlock(accumulated.toString())
            val savedMsg = initialAssistantMsg.copy(
                content = finalResponse.ifBlank { if (accumulated.isNotBlank()) accumulated.toString() else "No response generated." },
                thinkContent = finalThink,
                isThinkExpanded = isThinkMode.value,
                tokensPerSecond = tps,
                generationDurationMs = durationMs
            )

            chatRepository.saveMessage(savedMsg)
            _streamingMessage.value = null
            _isGenerating.value = false
        }
    }

    fun stopGeneration() {
        inferenceEngine.stopGeneration()
        generationJob?.cancel()

        val streaming = _streamingMessage.value
        if (streaming != null && streaming.content.isNotBlank()) {
            viewModelScope.launch {
                chatRepository.saveMessage(streaming)
                _streamingMessage.value = null
                _isGenerating.value = false
            }
        } else {
            _streamingMessage.value = null
            _isGenerating.value = false
        }
    }

    private fun extractThinkBlock(text: String): Pair<String?, String> {
        val thinkStart = text.indexOf("<think>")
        if (thinkStart == -1) return Pair(null, text)

        val thinkEnd = text.indexOf("</think>", thinkStart + 7)
        return if (thinkEnd != -1) {
            val think = text.substring(thinkStart + 7, thinkEnd).trim()
            val remaining = (text.substring(0, thinkStart) + text.substring(thinkEnd + 8)).trim()
            Pair(think, remaining)
        } else {
            // Still streaming think block
            val think = text.substring(thinkStart + 7).trim()
            val remaining = text.substring(0, thinkStart).trim()
            Pair(think, remaining)
        }
    }

    // Settings actions
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { preferencesRepository.setThemeMode(mode) }
    }

    fun setContextSize(size: Int) {
        viewModelScope.launch { preferencesRepository.setContextSize(size) }
    }

    fun setCpuThreads(threads: Int) {
        viewModelScope.launch { preferencesRepository.setCpuThreads(threads) }
    }

    fun setTemperature(temp: Float) {
        viewModelScope.launch { preferencesRepository.setTemperature(temp) }
    }

    fun setTopP(topP: Float) {
        viewModelScope.launch { preferencesRepository.setTopP(topP) }
    }
}
