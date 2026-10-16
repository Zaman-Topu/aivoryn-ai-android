package com.example.inference

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class GgufHeaderInfo(
    val isValidGguf: Boolean,
    val version: Int = 0,
    val tensorCount: Long = 0,
    val kvCount: Long = 0,
    val architecture: String = "Llama",
    val modelName: String = "GGUF Model",
    val quantType: String = "Q4_K_M",
    val contextLength: Int = 2048
)

object GgufMetadataParser {
    private const val GGUF_MAGIC = 0x46554747 // "GGUF" in Little Endian

    fun parse(file: File): GgufHeaderInfo {
        if (!file.exists() || file.length() < 32) {
            return GgufHeaderInfo(isValidGguf = false)
        }

        try {
            RandomAccessFile(file, "r").use { raf ->
                val headerBuffer = ByteArray(24)
                raf.readFully(headerBuffer)
                val bb = ByteBuffer.wrap(headerBuffer).order(ByteOrder.LITTLE_ENDIAN)

                val magic = bb.int
                if (magic != GGUF_MAGIC) {
                    return GgufHeaderInfo(isValidGguf = false)
                }

                val version = bb.int
                val tensorCount = bb.long
                val kvCount = bb.long

                var architecture = "Llama"
                var modelName = file.nameWithoutExtension
                var quantType = guessQuantFromFilename(file.name)
                var contextLength = 2048

                // Read up to first 128KB to extract common KV pairs safely
                val maxKvBytes = minOf(128 * 1024L, file.length() - 24).toInt()
                val kvBytes = ByteArray(maxKvBytes)
                raf.seek(24)
                val bytesRead = raf.read(kvBytes)

                if (bytesRead > 0) {
                    val kvBb = ByteBuffer.wrap(kvBytes, 0, bytesRead).order(ByteOrder.LITTLE_ENDIAN)
                    var parsedKvs = 0
                    while (kvBb.remaining() >= 8 && parsedKvs < kvCount && parsedKvs < 50) {
                        try {
                            val keyLen = kvBb.long.toInt()
                            if (keyLen <= 0 || keyLen > 256 || kvBb.remaining() < keyLen + 4) break
                            val keyBytes = ByteArray(keyLen)
                            kvBb.get(keyBytes)
                            val key = String(keyBytes, Charsets.UTF_8)

                            val valType = kvBb.int
                            when (valType) {
                                8 -> { // GGUF_TYPE_STRING
                                    if (kvBb.remaining() < 8) break
                                    val strLen = kvBb.long.toInt()
                                    if (strLen < 0 || strLen > 1024 || kvBb.remaining() < strLen) break
                                    val strBytes = ByteArray(strLen)
                                    kvBb.get(strBytes)
                                    val strVal = String(strBytes, Charsets.UTF_8)
                                    if (key == "general.architecture") architecture = strVal.replaceFirstChar { it.uppercase() }
                                    if (key == "general.name") modelName = strVal
                                }
                                4, 5 -> { // UINT32 or INT32
                                    if (kvBb.remaining() < 4) break
                                    val intVal = kvBb.int
                                    if (key.endsWith(".context_length")) contextLength = intVal
                                    if (key == "general.file_type") {
                                        quantType = decodeFileType(intVal)
                                    }
                                }
                                6, 7 -> { // UINT64 or INT64
                                    if (kvBb.remaining() < 8) break
                                    val longVal = kvBb.long
                                    if (key.endsWith(".context_length")) contextLength = longVal.toInt()
                                }
                                else -> {
                                    // Skip complex structures if we encountered unhandled types
                                    break
                                }
                            }
                            parsedKvs++
                        } catch (_: Exception) {
                            break
                        }
                    }
                }

                return GgufHeaderInfo(
                    isValidGguf = true,
                    version = version,
                    tensorCount = tensorCount,
                    kvCount = kvCount,
                    architecture = architecture,
                    modelName = modelName,
                    quantType = quantType,
                    contextLength = if (contextLength in 256..131072) contextLength else 2048
                )
            }
        } catch (e: Exception) {
            return GgufHeaderInfo(
                isValidGguf = true,
                architecture = "Llama",
                modelName = file.nameWithoutExtension,
                quantType = guessQuantFromFilename(file.name),
                contextLength = 2048
            )
        }
    }

    private fun guessQuantFromFilename(name: String): String {
        val upper = name.uppercase()
        return when {
            "Q4_K_M" in upper -> "Q4_K_M"
            "Q4_K_S" in upper -> "Q4_K_S"
            "Q5_K_M" in upper -> "Q5_K_M"
            "Q5_K_S" in upper -> "Q5_K_S"
            "Q3_K_M" in upper -> "Q3_K_M"
            "Q3_K_S" in upper -> "Q3_K_S"
            "Q2_K" in upper -> "Q2_K"
            "Q8_0" in upper -> "Q8_0"
            "Q4_0" in upper -> "Q4_0"
            "Q4_1" in upper -> "Q4_1"
            "Q5_0" in upper -> "Q5_0"
            "IQ3_M" in upper -> "IQ3_M"
            "IQ2_XS" in upper -> "IQ2_XS"
            "F16" in upper -> "F16"
            else -> "Q4_K_M"
        }
    }

    private fun decodeFileType(ft: Int): String {
        return when (ft) {
            0 -> "F32"
            1 -> "F16"
            2 -> "Q4_0"
            3 -> "Q4_1"
            7 -> "Q8_0"
            8 -> "Q5_0"
            9 -> "Q5_1"
            10 -> "Q2_K"
            11 -> "Q3_K_S"
            12 -> "Q3_K_M"
            13 -> "Q3_K_L"
            14 -> "Q4_K_S"
            15 -> "Q4_K_M"
            16 -> "Q5_K_S"
            17 -> "Q5_K_M"
            18 -> "Q6_K"
            else -> "Quant ($ft)"
        }
    }
}
