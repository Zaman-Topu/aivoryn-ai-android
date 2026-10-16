package com.example.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

data class ExtractedDocument(
    val fileName: String,
    val fileSizeFormatted: String,
    val content: String,
    val isTruncated: Boolean
)

object DocumentTextExtractor {
    private const val MAX_CHARS = 12000 // Keep within context bounds for low-RAM inference

    suspend fun extract(context: Context, uri: Uri): Result<ExtractedDocument> = withContext(Dispatchers.IO) {
        try {
            var fileName = "Document"
            var fileSize = 0L

            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (cursor.moveToFirst()) {
                    if (nameIndex != -1) fileName = cursor.getString(nameIndex) ?: "Document"
                    if (sizeIndex != -1) fileSize = cursor.getLong(sizeIndex)
                }
            }

            val sb = StringBuilder()
            var isTruncated = false

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                    var line = reader.readLine()
                    while (line != null) {
                        if (sb.length + line.length > MAX_CHARS) {
                            isTruncated = true
                            break
                        }
                        sb.append(line).append("\n")
                        line = reader.readLine()
                    }
                }
            } ?: return@withContext Result.failure(Exception("Unable to open file stream"))

            val sizeKb = fileSize / 1024
            val sizeFormatted = if (sizeKb > 1024) "${sizeKb / 1024} MB" else "$sizeKb KB"

            Result.success(
                ExtractedDocument(
                    fileName = fileName,
                    fileSizeFormatted = sizeFormatted,
                    content = sb.toString(),
                    isTruncated = isTruncated
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
