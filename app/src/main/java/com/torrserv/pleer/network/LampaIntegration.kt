package com.torrserv.pleer.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LampaIntegration(private val context: Context) {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private var lampaUrl: String = "http://localhost:8085"

    /**
     * Отправляет команду воспроизведения в Lampa
     */
    fun sendPlayCommand(url: String, title: String): Boolean {
        return try {
            val playbackData = """
                {
                    "action": "play",
                    "url": "$url",
                    "title": "$title",
                    "player": "pleer"
                }
            """.trimIndent()

            val request = Request.Builder()
                .url("$lampaUrl/api/player/play")
                .post(okhttp3.RequestBody.create(
                    okhttp3.MediaType.parse("application/json"),
                    playbackData
                ))
                .build()

            val response = httpClient.newCall(request).execute()
            response.isSuccessful.also {
                if (it) {
                    Timber.d("Play command sent to Lampa: $title")
                } else {
                    Timber.w("Lampa API error: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error sending play command to Lampa")
            false
        }
    }

    /**
     * Получает текущий статус плеера из Lampa
     */
    fun getPlayerStatus(): String {
        return try {
            val request = Request.Builder()
                .url("$lampaUrl/api/player/status")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string() ?: "unknown"
            } else {
                "error"
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting player status")
            "offline"
        }
    }

    /**
     * Устанавливает URL Lampa сервера
     */
    fun setLampaUrl(url: String) {
        this.lampaUrl = url
        Timber.d("Lampa URL updated to: $url")
    }

    fun getLampaUrl(): String = lampaUrl
}
