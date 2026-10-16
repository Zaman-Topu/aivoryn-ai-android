package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "models")
data class ModelEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val filePath: String,
    val fileSizeBytes: Long,
    val quantType: String = "Unknown",
    val architecture: String = "Llama",
    val contextLength: Int = 2048,
    val isActive: Boolean = false,
    val isLoadedInRam: Boolean = false,
    val importedAt: Long = System.currentTimeMillis()
)
