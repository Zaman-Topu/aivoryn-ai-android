package com.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.theme.ClaudeIcons
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.MessageEntity
import com.example.ui.components.AddToChatSheet
import com.example.ui.components.BrandingHeader
import com.example.ui.components.ClaudeComposer
import com.example.ui.components.ClaudeDrawerContent
import com.example.ui.components.MessageItem
import com.example.ui.components.ModelSelectorSheet
import com.example.ui.theme.ClaudeCoral
import com.example.ui.theme.ClaudeDarkBackground
import com.example.ui.theme.ClaudeDarkSurface
import com.example.ui.theme.ClaudeTextPrimary
import com.example.ui.theme.ClaudeTextSecondary
import com.example.util.DocumentTextExtractor
import com.example.util.SpeechRecognitionHelper
import kotlinx.coroutines.launch

enum class ScreenState {
    CHAT,
    MODEL_MANAGER,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    modelManagerViewModel: ModelManagerViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var currentScreen by remember { mutableStateOf(ScreenState.CHAT) }
    var isIncognito by remember { mutableStateOf(false) }

    // Bottom Sheets state
    var showAddSheet by remember { mutableStateOf(false) }
    var showModelSheet by remember { mutableStateOf(false) }
    var effortLevel by remember { mutableStateOf("Medium") }

    // Chat state
    val conversations by chatViewModel.conversations.collectAsStateWithLifecycle()
    val currentConvId by chatViewModel.currentConversationId.collectAsStateWithLifecycle()
    val messages by chatViewModel.currentMessages.collectAsStateWithLifecycle()
    val activeModel by chatViewModel.activeModel.collectAsStateWithLifecycle()
    val isGenerating by chatViewModel.isGenerating.collectAsStateWithLifecycle()
    val streamingMessage by chatViewModel.streamingMessage.collectAsStateWithLifecycle()
    val composerText by chatViewModel.composerText.collectAsStateWithLifecycle()
    val isSearchMode by chatViewModel.isSearchMode.collectAsStateWithLifecycle()
    val attachedDoc by chatViewModel.attachedDocument.collectAsStateWithLifecycle()
    val isListening by chatViewModel.isListening.collectAsStateWithLifecycle()
    val preferences by chatViewModel.preferences.collectAsStateWithLifecycle()

    // Model manager state
    val allModels by modelManagerViewModel.models.collectAsStateWithLifecycle()
    val isImporting by modelManagerViewModel.isImporting.collectAsStateWithLifecycle()
    val importError by modelManagerViewModel.importError.collectAsStateWithLifecycle()
    val downloadStates by modelManagerViewModel.downloadStates.collectAsStateWithLifecycle()
    val statusMessage by modelManagerViewModel.statusMessage.collectAsStateWithLifecycle()
    val huggingFaceCatalog = modelManagerViewModel.huggingFaceCatalog

    // File Picker for Document Attachment (TXT, PDF, MD, code)
    val docPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val result = DocumentTextExtractor.extract(context, uri)
                if (result.isSuccess) {
                    chatViewModel.attachDocument(result.getOrThrow())
                } else {
                    Toast.makeText(context, "Failed to read file: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Zero-permission Android Photo Picker for Photos (Google Play compliant)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val result = DocumentTextExtractor.extract(context, uri)
                if (result.isSuccess) {
                    chatViewModel.attachDocument(result.getOrThrow())
                } else {
                    Toast.makeText(context, "Photo attached: ${uri.lastPathSegment}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // GGUF Model Import Picker
    val ggufPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            modelManagerViewModel.importGgufFromUri(uri)
        }
    }

    // Speech Recognizer helper
    val speechHelper = remember {
        SpeechRecognitionHelper(
            context = context,
            onResult = { recognizedText ->
                chatViewModel.onSpeechResult(recognizedText)
            },
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            },
            onListeningStateChanged = { listening ->
                chatViewModel.isListening.value = listening
            }
        )
    }

    // Audio Permission Launcher
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            speechHelper.startListening()
        } else {
            Toast.makeText(context, "Microphone permission is required for voice input.", Toast.LENGTH_SHORT).show()
        }
    }

    // Back handler for sub-screens & drawer
    BackHandler(enabled = drawerState.isOpen || currentScreen != ScreenState.CHAT) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            currentScreen = ScreenState.CHAT
        }
    }

    when (currentScreen) {
        ScreenState.MODEL_MANAGER -> {
            ModelManagerScreen(
                models = allModels,
                activeModel = activeModel,
                downloadStates = downloadStates,
                huggingFaceCatalog = huggingFaceCatalog,
                isImporting = isImporting,
                importError = importError,
                statusMessage = statusMessage,
                onImportClick = { ggufPickerLauncher.launch(arrayOf("*/*")) },
                onSelectActiveModel = { id -> modelManagerViewModel.selectActiveModel(id) },
                onLoadModelIntoRam = { model -> modelManagerViewModel.loadModelIntoRam(model) },
                onUnloadModel = { modelManagerViewModel.unloadModel() },
                onDeleteModel = { model -> modelManagerViewModel.deleteModel(model) },
                onDownloadHfModel = { hfModel -> modelManagerViewModel.downloadHuggingFaceModel(hfModel) },
                onCancelDownload = { id -> modelManagerViewModel.cancelDownload(id) },
                onDownloadCustomUrl = { url, name -> modelManagerViewModel.downloadCustomUrl(url, name) },
                onDismissError = { modelManagerViewModel.dismissError() },
                onDismissStatus = { modelManagerViewModel.dismissStatusMessage() },
                onBackClick = { currentScreen = ScreenState.CHAT }
            )
        }

        ScreenState.SETTINGS -> {
            SettingsScreen(
                preferences = preferences,
                onThemeModeChanged = { chatViewModel.setThemeMode(it) },
                onContextSizeChanged = { chatViewModel.setContextSize(it) },
                onCpuThreadsChanged = { chatViewModel.setCpuThreads(it) },
                onTemperatureChanged = { chatViewModel.setTemperature(it) },
                onTopPChanged = { chatViewModel.setTopP(it) },
                onClearAllConversations = { chatViewModel.clearAllConversations() },
                onBackClick = { currentScreen = ScreenState.CHAT }
            )
        }

        ScreenState.CHAT -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = ClaudeDarkBackground
                    ) {
                        ClaudeDrawerContent(
                            conversations = conversations,
                            currentConversationId = currentConvId,
                            onSelectConversation = { id ->
                                isIncognito = false
                                chatViewModel.selectConversation(id)
                                scope.launch { drawerState.close() }
                            },
                            onNewChat = {
                                isIncognito = false
                                chatViewModel.startNewChat()
                                scope.launch { drawerState.close() }
                            },
                            onOpenProjects = {
                                Toast.makeText(context, "Projects feature coming soon in local GGUF v2", Toast.LENGTH_SHORT).show()
                            },
                            onOpenCode = {
                                Toast.makeText(context, "Code interpreter view: open any conversation with code blocks", Toast.LENGTH_SHORT).show()
                            },
                            onOpenArtifacts = {
                                Toast.makeText(context, "Artifacts: code and text blocks extracted from chats", Toast.LENGTH_SHORT).show()
                            },
                            onOpenSettings = {
                                scope.launch {
                                    drawerState.close()
                                    currentScreen = ScreenState.SETTINGS
                                }
                            },
                            onDeleteConversation = { id ->
                                chatViewModel.deleteConversation(id)
                            }
                        )
                    }
                }
            ) {
                Scaffold(
                    containerColor = ClaudeDarkBackground,
                    topBar = {
                        TopAppBar(
                            title = {
                                if (isIncognito) {
                                    Text(
                                        text = "Incognito chat",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 15.sp
                                        ),
                                        color = ClaudeTextSecondary
                                    )
                                } else if (currentConvId != null) {
                                    val currentTitle = conversations.find { it.id == currentConvId }?.title ?: ""
                                    Text(
                                        text = currentTitle,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = ClaudeTextPrimary
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } },
                                    modifier = Modifier.testTag("app_bar_drawer_button")
                                ) {
                                    Icon(
                                        imageVector = ClaudeIcons.Menu,
                                        contentDescription = "Open drawer",
                                        tint = ClaudeTextPrimary
                                    )
                                }
                            },
                            actions = {
                                if (isIncognito) {
                                    // Close incognito button (Screenshot 2: 'X' on top right)
                                    IconButton(
                                        onClick = { isIncognito = false },
                                        modifier = Modifier.testTag("close_incognito_button")
                                    ) {
                                        Icon(
                                            imageVector = ClaudeIcons.Close,
                                            contentDescription = "Exit Incognito",
                                            tint = ClaudeTextPrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                } else {
                                    // Incognito Ghost toggle icon (Screenshot 1: custom Claude ghost icon)
                                    IconButton(
                                        onClick = {
                                            isIncognito = true
                                            chatViewModel.startNewChat()
                                        },
                                        modifier = Modifier.testTag("incognito_ghost_button")
                                    ) {
                                        Icon(
                                            imageVector = ClaudeIcons.Ghost,
                                            contentDescription = "Incognito Mode",
                                            tint = ClaudeTextPrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = ClaudeDarkBackground
                            )
                        )
                    },
                    bottomBar = {
                        ClaudeComposer(
                            text = composerText,
                            onTextChanged = { chatViewModel.composerText.value = it },
                            isGenerating = isGenerating,
                            isIncognito = isIncognito,
                            activeModel = activeModel,
                            attachedDocument = attachedDoc,
                            onRemoveAttachment = { chatViewModel.removeAttachment() },
                            isListening = isListening,
                            onOpenAddSheet = { showAddSheet = true },
                            onOpenModelSheet = { showModelSheet = true },
                            onMicClick = {
                                if (isListening) {
                                    speechHelper.stopListening()
                                } else {
                                    val hasPermission = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (hasPermission) {
                                        speechHelper.startListening()
                                    } else {
                                        audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                }
                            },
                            onVoiceModeClick = {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                                if (hasPermission) {
                                    speechHelper.startListening()
                                    Toast.makeText(context, "Voice mode active — speak now", Toast.LENGTH_SHORT).show()
                                } else {
                                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            onSendClick = { chatViewModel.sendMessage() },
                            onStopClick = { chatViewModel.stopGeneration() }
                        )
                    }
                ) { innerPadding ->
                    val listState = rememberLazyListState()
                    val isImeVisible = WindowInsets.isImeVisible

                    LaunchedEffect(messages.size, streamingMessage?.content?.length) {
                        if (messages.isNotEmpty() || streamingMessage != null) {
                            val totalItems = messages.size + if (streamingMessage != null) 1 else 0
                            listState.animateScrollToItem((totalItems - 1).coerceAtLeast(0))
                        }
                    }

                    LaunchedEffect(isImeVisible) {
                        if (isImeVisible && (messages.isNotEmpty() || streamingMessage != null)) {
                            val totalItems = messages.size + if (streamingMessage != null) 1 else 0
                            listState.animateScrollToItem((totalItems - 1).coerceAtLeast(0))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ClaudeDarkBackground)
                            .padding(innerPadding)
                    ) {
                        if (messages.isEmpty() && streamingMessage == null) {
                            // Claude-Style Welcome Screen (Screenshot 1 & 2)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                contentAlignment = Alignment.Center
                            ) {
                                BrandingHeader(
                                    activeModel = activeModel,
                                    isIncognito = isIncognito,
                                    userName = "Topu",
                                    onOpenModelManager = { currentScreen = ScreenState.MODEL_MANAGER },
                                    onLearnMoreIncognito = {
                                        Toast.makeText(context, "Incognito conversations are strictly in-memory and never saved to Room database.", Toast.LENGTH_LONG).show()
                                    },
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 8.dp)
                            ) {
                                items(messages, key = { it.id }) { message ->
                                    MessageItem(
                                        message = message,
                                        isStreaming = false,
                                        onRegenerate = { chatViewModel.regenerate() }
                                    )
                                }

                                streamingMessage?.let { streamMsg ->
                                    item(key = "streaming_message") {
                                        MessageItem(
                                            message = streamMsg,
                                            isStreaming = true
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet: Add to chat (Screenshot 5)
    if (showAddSheet) {
        AddToChatSheet(
            onDismiss = { showAddSheet = false },
            onCameraClick = {
                Toast.makeText(context, "Camera capture: select Photo or Document", Toast.LENGTH_SHORT).show()
                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onPhotosClick = {
                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onFilesClick = {
                docPickerLauncher.launch(arrayOf("text/*", "application/pdf", "*/*"))
            },
            isSearchMode = isSearchMode,
            onToggleSearch = { chatViewModel.toggleSearchMode() },
            onOpenConnectors = {
                currentScreen = ScreenState.MODEL_MANAGER
            }
        )
    }

    // Modal Bottom Sheet: Select model (Screenshot 3)
    if (showModelSheet) {
        ModelSelectorSheet(
            models = allModels,
            activeModel = activeModel,
            effortLevel = effortLevel,
            onEffortLevelCycle = {
                effortLevel = when (effortLevel) {
                    "Low" -> "Medium"
                    "Medium" -> "High"
                    else -> "Low"
                }
                chatViewModel.toggleThinkMode()
            },
            onSelectModel = { id ->
                modelManagerViewModel.selectActiveModel(id)
            },
            onOpenMoreModels = {
                currentScreen = ScreenState.MODEL_MANAGER
            },
            onDismiss = { showModelSheet = false }
        )
    }
}
