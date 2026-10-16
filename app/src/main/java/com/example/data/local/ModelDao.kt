package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ModelDao {
    @Query("SELECT * FROM models ORDER BY importedAt DESC")
    fun getAllModels(): Flow<List<ModelEntity>>

    @Query("SELECT * FROM models WHERE isActive = 1 LIMIT 1")
    fun getActiveModel(): Flow<ModelEntity?>

    @Query("SELECT * FROM models WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveModelDirect(): ModelEntity?

    @Query("SELECT * FROM models WHERE id = :id LIMIT 1")
    suspend fun getModelById(id: String): ModelEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: ModelEntity)

    @Update
    suspend fun updateModel(model: ModelEntity)

    @Query("UPDATE models SET isActive = 0")
    suspend fun clearActiveModel()

    @Query("UPDATE models SET isActive = 1 WHERE id = :id")
    suspend fun setActiveModel(id: String)

    @Query("UPDATE models SET isLoadedInRam = :loaded WHERE id = :id")
    suspend fun setLoadedInRam(id: String, loaded: Boolean)

    @Query("UPDATE models SET isLoadedInRam = 0")
    suspend fun unloadAllModels()

    @Query("DELETE FROM models WHERE id = :id")
    suspend fun deleteModelById(id: String)
}
