package com.example.data.repository

import com.example.data.local.ModelDao
import com.example.data.local.ModelEntity
import kotlinx.coroutines.flow.Flow
import java.io.File

class ModelRepository(
    private val modelDao: ModelDao
) {
    val allModels: Flow<List<ModelEntity>> = modelDao.getAllModels()
    val activeModel: Flow<ModelEntity?> = modelDao.getActiveModel()

    suspend fun getActiveModelDirect(): ModelEntity? = modelDao.getActiveModelDirect()

    suspend fun getModelById(id: String): ModelEntity? = modelDao.getModelById(id)

    suspend fun addModel(model: ModelEntity) {
        modelDao.insertModel(model)
        // If this is the only model, make it active
        if (modelDao.getActiveModelDirect() == null) {
            modelDao.setActiveModel(model.id)
        }
    }

    suspend fun selectActiveModel(id: String) {
        modelDao.clearActiveModel()
        modelDao.setActiveModel(id)
    }

    suspend fun setLoadedInRam(id: String, loaded: Boolean) {
        modelDao.setLoadedInRam(id, loaded)
    }

    suspend fun unloadAll() {
        modelDao.unloadAllModels()
    }

    suspend fun deleteModel(model: ModelEntity) {
        modelDao.deleteModelById(model.id)
        // Also delete file from disk if stored internally
        try {
            val file = File(model.filePath)
            if (file.exists() && file.isFile) {
                file.delete()
            }
        } catch (_: Exception) { }
    }
}
