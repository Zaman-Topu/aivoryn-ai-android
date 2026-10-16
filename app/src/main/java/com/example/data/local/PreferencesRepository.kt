package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "aivoryn_preferences")

data class AppPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val contextSize: Int = 1024, // Conservative default for 4GB RAM devices (MT6765)
    val cpuThreads: Int = 2,     // 2-4 threads optimal for MediaTek MT6765
    val temperature: Float = 0.7f,
    val topP: Float = 0.9f,
    val isThinkEnabledByDefault: Boolean = false,
    val isSearchEnabledByDefault: Boolean = false
)

class PreferencesRepository(private val context: Context) {
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CONTEXT_SIZE = intPreferencesKey("context_size")
        val CPU_THREADS = intPreferencesKey("cpu_threads")
        val TEMPERATURE = floatPreferencesKey("temperature")
        val TOP_P = floatPreferencesKey("top_p")
    }

    val preferencesFlow: Flow<AppPreferences> = context.dataStore.data.map { preferences ->
        val themeString = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        val themeMode = try {
            ThemeMode.valueOf(themeString)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }

        AppPreferences(
            themeMode = themeMode,
            contextSize = preferences[PreferencesKeys.CONTEXT_SIZE] ?: 1024,
            cpuThreads = preferences[PreferencesKeys.CPU_THREADS] ?: 2,
            temperature = preferences[PreferencesKeys.TEMPERATURE] ?: 0.7f,
            topP = preferences[PreferencesKeys.TOP_P] ?: 0.9f
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun setContextSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CONTEXT_SIZE] = size
        }
    }

    suspend fun setCpuThreads(threads: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CPU_THREADS] = threads
        }
    }

    suspend fun setTemperature(temp: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TEMPERATURE] = temp
        }
    }

    suspend fun setTopP(topP: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOP_P] = topP
        }
    }
}
