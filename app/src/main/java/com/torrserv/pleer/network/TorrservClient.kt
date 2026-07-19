package com.torrserv.pleer.network

import android.content.Context
import com.google.gson.Gson
import com.torrserv.pleer.viewmodel.TorrservStream
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.util.concurrent.TimeUnit

interface TorrservApi {
    @GET("/api/search")
    suspend fun search(@Query("query") query: String): SearchResponse

    @GET("/api/top")
    suspend fun getTop(@Query("category") category: String = ""): SearchResponse
}

data class SearchResponse(
    val results: List<StreamResult> = emptyList(),
    val error: String? = null
)

data class StreamResult(
    val id: String,
    val name: String,
    val url: String,
    val poster: String? = null,
    val quality: String = "HD",
    val seeds: Int = 0,
    val peers: Int = 0
)

class TorrservClient(private val context: Context) {

    private var torrservUrl: String = "http://localhost:8090"
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(torrservUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    private val api: TorrservApi = retrofit.create(TorrservApi::class.java)

    suspend fun searchStreams(query: String): List<TorrservStream> {
        return try {
            val response = api.search(query)
            if (response.error != null) {
                Timber.e("Torrserv error: ${response.error}")
                emptyList()
            } else {
                response.results.map { result ->
                    TorrservStream(
                        id = result.id,
                        name = result.name,
                        url = result.url,
                        posterUrl = result.poster,
                        quality = result.quality,
                        seeds = result.seeds,
                        peers = result.peers
                    )
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error searching streams")
            emptyList()
        }
    }

    suspend fun getTopStreams(): List<TorrservStream> {
        return try {
            val response = api.getTop()
            response.results.map { result ->
                TorrservStream(
                    id = result.id,
                    name = result.name,
                    url = result.url,
                    posterUrl = result.poster,
                    quality = result.quality,
                    seeds = result.seeds,
                    peers = result.peers
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching top streams")
            emptyList()
        }
    }

    fun setTorrservUrl(url: String) {
        this.torrservUrl = url
        Timber.d("Torrserv URL updated to: $url")
    }

    fun getTorrservUrl(): String = torrservUrl
}
