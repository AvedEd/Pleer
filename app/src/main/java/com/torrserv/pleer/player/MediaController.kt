package com.torrserv.pleer.player

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaMetadata
import timber.log.Timber

class MediaController(
    private val exoPlayer: ExoPlayer,
    private val context: Context
) {

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        exoPlayer.playWhenReady = false
        Timber.d("MediaController initialized")
    }

    fun setMetadata(title: String, subtitle: String? = null, duration: Long = 0) {
        val metadata = MediaMetadata.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDurationMs(duration)
            .build()

        exoPlayer.mediaMetadata = metadata
    }

    fun getAvailableCodecs(): List<String> {
        val codecs = mutableListOf<String>()
        try {
            // H.264, H.265 (HEVC), VP8, VP9
            codecs.addAll(listOf("H.264", "H.265", "VP8", "VP9", "AV1"))
            
            // Audio codecs
            codecs.addAll(listOf("AAC", "MP3", "FLAC", "Opus", "Vorbis"))
        } catch (e: Exception) {
            Timber.e(e, "Error getting available codecs")
        }
        return codecs
    }

    fun getPlayerStats(): PlayerStats {
        return PlayerStats(
            currentPosition = exoPlayer.currentPosition,
            duration = exoPlayer.duration,
            bufferedPosition = exoPlayer.bufferedPosition,
            isPlaying = exoPlayer.isPlaying,
            playbackState = exoPlayer.playbackState,
            videoSize = exoPlayer.videoSize,
            audioSessionId = exoPlayer.audioSessionId
        )
    }
}

data class PlayerStats(
    val currentPosition: Long,
    val duration: Long,
    val bufferedPosition: Long,
    val isPlaying: Boolean,
    val playbackState: Int,
    val videoSize: androidx.media3.common.VideoSize,
    val audioSessionId: Int
)