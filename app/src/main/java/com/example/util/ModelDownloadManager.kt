package com.example.util

import android.content.Context
import com.example.data.model.HuggingFaceModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

data class DownloadProgress(
    val modelId: String,
    val modelName: String,
    val bytesDownloaded: Long,
    val totalBytes: Long,
    val progressPercent: Int,
    val speedMbPerSec: Float,
    val isComplete: Boolean = false,
    val error: String? = null
)

class ModelDownloadManager(private val context: Context) {

    private val _downloadStates = MutableStateFlow<Map<String, DownloadProgress>>(emptyMap())
    val downloadStates: StateFlow<Map<String, DownloadProgress>> = _downloadStates.asStateFlow()

    private val activeJobs = ConcurrentHashMap<String, Job>()

    fun isDownloading(modelId: String): Boolean {
        return _downloadStates.value[modelId]?.let { !it.isComplete && it.error == null } ?: false
    }

    suspend fun downloadModel(
        model: HuggingFaceModel,
        onSuccess: suspend (File) -> Unit,
        onError: suspend (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        val modelsDir = File(context.filesDir, "models").apply { mkdirs() }
        val targetFile = File(modelsDir, model.fileName)
        val tempFile = File(modelsDir, "${model.fileName}.download")

        updateProgress(
            DownloadProgress(
                modelId = model.id,
                modelName = model.name,
                bytesDownloaded = 0,
                totalBytes = model.sizeBytes,
                progressPercent = 0,
                speedMbPerSec = 0f
            )
        )

        try {
            var currentUrl = model.downloadUrl
            var connection: HttpURLConnection? = null
            var redirectsCount = 0

            // Follow Hugging Face CDN redirects
            while (redirectsCount < 6) {
                val url = URL(currentUrl)
                connection = (url.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 15000
                    readTimeout = 25000
                    instanceFollowRedirects = true
                    setRequestProperty("User-Agent", "AivorynAI-Android/1.0")
                }

                val responseCode = connection.responseCode
                if (responseCode in 300..399) {
                    val location = connection.getHeaderField("Location")
                    if (location != null) {
                        currentUrl = location
                        connection.disconnect()
                        redirectsCount++
                        continue
                    }
                }
                break
            }

            val finalConnection = connection ?: throw Exception("Failed to establish network connection")
            if (finalConnection.responseCode !in 200..299) {
                throw Exception("HTTP Error: ${finalConnection.responseCode} ${finalConnection.responseMessage}")
            }

            val remoteLength = finalConnection.contentLengthLong
            val totalBytes = if (remoteLength > 0) remoteLength else model.sizeBytes

            var bytesCopied = 0L
            var lastUpdateTime = System.currentTimeMillis()
            var bytesSinceLastUpdate = 0L
            var currentSpeed = 0f

            val buffer = ByteArray(16 * 1024)
            finalConnection.inputStream.use { input: InputStream ->
                FileOutputStream(tempFile).use { output ->
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        bytesCopied += bytesRead
                        bytesSinceLastUpdate += bytesRead

                        val now = System.currentTimeMillis()
                        val elapsed = now - lastUpdateTime
                        if (elapsed >= 300) {
                            currentSpeed = (bytesSinceLastUpdate.toFloat() / (1024f * 1024f)) / (elapsed.toFloat() / 1000f)
                            lastUpdateTime = now
                            bytesSinceLastUpdate = 0L

                            val percent = if (totalBytes > 0) {
                                ((bytesCopied * 100) / totalBytes).toInt().coerceIn(0, 99)
                            } else 0

                            updateProgress(
                                DownloadProgress(
                                    modelId = model.id,
                                    modelName = model.name,
                                    bytesDownloaded = bytesCopied,
                                    totalBytes = totalBytes,
                                    progressPercent = percent,
                                    speedMbPerSec = currentSpeed
                                )
                            )
                        }
                    }
                    output.flush()
                }
            }

            // Move temp file to permanent target file
            if (targetFile.exists()) targetFile.delete()
            tempFile.renameTo(targetFile)

            updateProgress(
                DownloadProgress(
                    modelId = model.id,
                    modelName = model.name,
                    bytesDownloaded = targetFile.length(),
                    totalBytes = targetFile.length(),
                    progressPercent = 100,
                    speedMbPerSec = 0f,
                    isComplete = true
                )
            )

            onSuccess(targetFile)
        } catch (e: Exception) {
            if (tempFile.exists()) tempFile.delete()
            val errorMsg = if (e is CancellationException) "Download cancelled" else (e.message ?: "Download failed")
            updateProgress(
                DownloadProgress(
                    modelId = model.id,
                    modelName = model.name,
                    bytesDownloaded = 0,
                    totalBytes = model.sizeBytes,
                    progressPercent = 0,
                    speedMbPerSec = 0f,
                    isComplete = false,
                    error = errorMsg
                )
            )
            onError(errorMsg)
        }
    }

    fun cancelDownload(modelId: String) {
        activeJobs[modelId]?.cancel()
        activeJobs.remove(modelId)
        val current = _downloadStates.value.toMutableMap()
        current.remove(modelId)
        _downloadStates.value = current
    }

    private fun updateProgress(progress: DownloadProgress) {
        val current = _downloadStates.value.toMutableMap()
        current[progress.modelId] = progress
        _downloadStates.value = current
    }
}
