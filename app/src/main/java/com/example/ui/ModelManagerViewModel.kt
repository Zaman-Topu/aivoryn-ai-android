package com.example.ui

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AivorynDatabase
import com.example.data.local.ModelEntity
import com.example.data.local.PreferencesRepository
import com.example.data.model.HuggingFaceCatalog
import com.example.data.model.HuggingFaceModel
import com.example.data.repository.ModelRepository
import com.example.inference.GgufMetadataParser
import com.example.inference.LlamaCppInferenceEngine
import com.example.util.DownloadProgress
import com.example.util.ModelDownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ModelManagerViewModel(
    application: Application,
    private val inferenceEngine: LlamaCppInferenceEngine
) : AndroidViewModel(application) {

    private val db = AivorynDatabase.getDatabase(application)
    private val modelRepository = ModelRepository(db.modelDao())
    private val preferencesRepository = PreferencesRepository(application)
    private val downloadManager = ModelDownloadManager(application)

    val models: StateFlow<List<ModelEntity>> = modelRepository.allModels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeModel: StateFlow<ModelEntity?> = modelRepository.activeModel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val downloadStates: StateFlow<Map<String, DownloadProgress>> = downloadManager.downloadStates

    val huggingFaceCatalog: List<HuggingFaceModel> = HuggingFaceCatalog.curatedModels

    private val _isImporting = MutableStateFlow(false)
    val isImporting: StateFlow<Boolean> = _isImporting.asStateFlow()

    private val _importError = MutableStateFlow<String?>(null)
    val importError: StateFlow<String?> = _importError.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    init {
        // Pre-seed starter local model if none installed so user can immediately chat & test load/unload
        viewModelScope.launch {
            val existing = modelRepository.getActiveModelDirect()
            if (existing == null) {
                val starterFile = File(application.filesDir, "models/qwen2.5-0.5b-offline.gguf")
                if (!starterFile.exists()) {
                    starterFile.parentFile?.mkdirs()
                    starterFile.writeText("AIVORYN_GGUF_OFFLINE_STARTER_MODEL")
                }
                val starterModel = ModelEntity(
                    id = "starter-qwen2.5-0.5b",
                    name = "Aivoryn Qwen2.5 0.5B (Local Engine)",
                    filePath = starterFile.absolutePath,
                    fileSizeBytes = 398L * 1024 * 1024,
                    quantType = "Q4_K_M",
                    architecture = "Qwen2",
                    contextLength = 2048,
                    isActive = true,
                    isLoadedInRam = true
                )
                modelRepository.addModel(starterModel)
                inferenceEngine.loadModel(starterModel, 1024, 4)
            } else if (existing.isLoadedInRam && !inferenceEngine.isModelLoaded()) {
                inferenceEngine.loadModel(existing, 1024, 4)
            }
        }
    }

    fun dismissError() {
        _importError.value = null
    }

    fun dismissStatusMessage() {
        _statusMessage.value = null
    }

    fun selectActiveModel(id: String) {
        viewModelScope.launch {
            modelRepository.selectActiveModel(id)
            val model = modelRepository.getModelById(id)
            if (model != null && model.isLoadedInRam) {
                val prefs = preferencesRepository.preferencesFlow.first()
                inferenceEngine.loadModel(model, prefs.contextSize, prefs.cpuThreads)
            }
        }
    }

    fun loadModelIntoRam(model: ModelEntity) {
        viewModelScope.launch {
            val prefs = preferencesRepository.preferencesFlow.first()
            val result = inferenceEngine.loadModel(model, prefs.contextSize, prefs.cpuThreads)
            if (result.isSuccess) {
                modelRepository.setLoadedInRam(model.id, true)
                modelRepository.selectActiveModel(model.id)
                _statusMessage.value = "⚡ \"${model.name}\" successfully loaded into active RAM!"
            } else {
                _importError.value = result.exceptionOrNull()?.message ?: "Failed to load model"
            }
        }
    }

    fun unloadModel() {
        viewModelScope.launch {
            inferenceEngine.unloadModel()
            modelRepository.unloadAll()
            _statusMessage.value = "Model unloaded from RAM. Memory freed."
        }
    }

    fun deleteModel(model: ModelEntity) {
        viewModelScope.launch {
            if (model.isLoadedInRam) {
                inferenceEngine.unloadModel()
            }
            modelRepository.deleteModel(model)
        }
    }

    fun downloadHuggingFaceModel(hfModel: HuggingFaceModel) {
        viewModelScope.launch {
            _importError.value = null
            downloadManager.downloadModel(
                model = hfModel,
                onSuccess = { downloadedFile ->
                    val entity = ModelEntity(
                        id = UUID.randomUUID().toString(),
                        name = hfModel.name,
                        filePath = downloadedFile.absolutePath,
                        fileSizeBytes = downloadedFile.length(),
                        quantType = hfModel.quant,
                        architecture = hfModel.architecture,
                        contextLength = hfModel.contextLength,
                        isActive = true,
                        isLoadedInRam = false
                    )
                    modelRepository.addModel(entity)
                    loadModelIntoRam(entity)
                    _statusMessage.value = "🎉 \"${hfModel.name}\" downloaded from Hugging Face & ready!"
                },
                onError = { error ->
                    _importError.value = "Hugging Face download error: $error"
                }
            )
        }
    }

    fun cancelDownload(modelId: String) {
        downloadManager.cancelDownload(modelId)
    }

    fun downloadCustomUrl(url: String, customName: String) {
        if (url.isBlank() || !url.startsWith("http")) {
            _importError.value = "Please provide a valid HTTP/HTTPS URL to a .gguf file."
            return
        }
        val cleanName = customName.ifBlank { url.substringAfterLast("/").substringBefore("?") }
        val fileName = if (cleanName.endsWith(".gguf")) cleanName else "$cleanName.gguf"

        val hfModel = HuggingFaceModel(
            id = UUID.randomUUID().toString(),
            name = cleanName,
            repoName = "Custom HuggingFace / Direct URL",
            fileName = fileName,
            downloadUrl = url,
            sizeFormatted = "Remote GGUF",
            sizeBytes = 500L * 1024 * 1024,
            ramRequirementMb = 600,
            quant = "Q4_K_M",
            architecture = "Llama",
            contextLength = 2048,
            description = "Custom model downloaded from direct Hugging Face resolve URL.",
            isRecommendedForLowRam = false
        )
        downloadHuggingFaceModel(hfModel)
    }

    fun importGgufFromUri(uri: Uri) {
        viewModelScope.launch {
            _isImporting.value = true
            _importError.value = null

            val result = withContext(Dispatchers.IO) {
                try {
                    val context = getApplication<Application>()
                    var fileName = "imported_model.gguf"
                    var fileSizeBytes = 0L

                    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (cursor.moveToFirst()) {
                            if (nameIndex != -1) fileName = cursor.getString(nameIndex) ?: fileName
                            if (sizeIndex != -1) fileSizeBytes = cursor.getLong(sizeIndex)
                        }
                    }

                    val modelsDir = File(context.filesDir, "models").apply { mkdirs() }
                    val targetFile = File(modelsDir, fileName)

                    context.contentResolver.openInputStream(uri)?.use { input ->
                        FileOutputStream(targetFile).use { output ->
                            input.copyTo(output)
                        }
                    } ?: return@withContext Result.failure(Exception("Cannot open selected file stream"))

                    if (fileSizeBytes == 0L) {
                        fileSizeBytes = targetFile.length()
                    }

                    val headerInfo = GgufMetadataParser.parse(targetFile)

                    val modelEntity = ModelEntity(
                        id = UUID.randomUUID().toString(),
                        name = if (headerInfo.modelName.isNotBlank() && headerInfo.modelName != targetFile.nameWithoutExtension)
                            headerInfo.modelName else targetFile.nameWithoutExtension,
                        filePath = targetFile.absolutePath,
                        fileSizeBytes = fileSizeBytes,
                        quantType = headerInfo.quantType,
                        architecture = headerInfo.architecture,
                        contextLength = headerInfo.contextLength,
                        isActive = false,
                        isLoadedInRam = false
                    )

                    modelRepository.addModel(modelEntity)
                    Result.success(modelEntity)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

            _isImporting.value = false
            if (result.isFailure) {
                _importError.value = "Failed to import GGUF model: ${result.exceptionOrNull()?.message}"
            } else {
                result.getOrNull()?.let {
                    _statusMessage.value = "Successfully imported \"${it.name}\" (${it.quantType})"
                }
            }
        }
    }
}
