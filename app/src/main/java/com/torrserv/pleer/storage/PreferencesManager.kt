package com.torrserv.pleer.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

val Context.dataStore by preferencesDataStore(name = "pleer_settings")

class PreferencesManager(private val context: Context) {

    companion object {
        private val PLAYBACK_SPEED = floatPreferencesKey("playback_speed")
        private val TORRSERV_URL = stringPreferencesKey("torrserv_url")
        private val LAMPA_URL = stringPreferencesKey("lampa_url")
        private val BUFFER_SIZE = floatPreferencesKey("buffer_size")
        private val LAST_PLAYED_URL = stringPreferencesKey("last_played_url")
        private val LAST_POSITION = floatPreferencesKey("last_position")
    }

    suspend fun savePlaybackSpeed(speed: Float) {
        try {
            context.dataStore.edit { preferences ->
                preferences[PLAYBACK_SPEED] = speed
            }
            Timber.d("Saved playback speed: $speed")
        } catch (e: Exception) {
            Timber.e(e, "Error saving playback speed")
        }
    }

    suspend fun getPlaybackSpeed(): Float {
        return try {
            context.dataStore.data.map { preferences ->
                preferences[PLAYBACK_SPEED] ?: 1.0f
            }.collect { speed ->
                return@getPlaybackSpeed speed
            }
            1.0f
        } catch (e: Exception) {
            Timber.e(e, "Error getting playback speed")
            1.0f
        }
    }

    suspend fun saveTorrservUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[TORRSERV_URL] = url
        }
    }

    suspend fun getTorrservUrl(): String {
        return try {
            var result = "http://localhost:8090"
            context.dataStore.data.map { preferences ->
                preferences[TORRSERV_URL] ?: "http://localhost:8090"
            }.collect { url ->
                result = url
            }
            result
        } catch (e: Exception) {
            "http://localhost:8090"
        }
    }

    suspend fun saveLampaUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[LAMPA_URL] = url
        }
    }

    suspend fun getLampaUrl(): String {
        return try {
            var result = "http://localhost:8085"
            context.dataStore.data.map { preferences ->
                preferences[LAMPA_URL] ?: "http://localhost:8085"
            }.collect { url ->
                result = url
            }
            result
        } catch (e: Exception) {
            "http://localhost:8085"
        }
    }

    suspend fun saveBufferSize(size: Float) {
        context.dataStore.edit { preferences ->
            preferences[BUFFER_SIZE] = size
        }
    }

    suspend fun getBufferSize(): Float {
        return try {
            var result = 50f
            context.dataStore.data.map { preferences ->
                preferences[BUFFER_SIZE] ?: 50f
            }.collect { size ->
                result = size
            }
            result
        } catch (e: Exception) {
            50f
        }
    }

    suspend fun saveLastPlayedUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_PLAYED_URL] = url
        }
    }

    suspend fun getLastPlayedUrl(): String? {
        return try {
            var result: String? = null
            context.dataStore.data.map { preferences ->
                preferences[LAST_PLAYED_URL]
            }.collect { url ->
                result = url
            }
            result
        } catch (e: Exception) {
            null
        }
    }
}
