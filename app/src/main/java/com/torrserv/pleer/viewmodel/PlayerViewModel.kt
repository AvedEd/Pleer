package com.torrserv.pleer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.torrserv.pleer.network.TorrservClient
import com.torrserv.pleer.player.CodecManager
import com.torrserv.pleer.player.MediaController
import com.torrserv.pleer.storage.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import android.view.KeyEvent

data class PlayerUiState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val bufferedPosition: Long = 0L,
    val isShowingControls: Boolean = true,
    val currentTitle: String = "Pleer",
    val errorMessage: String? = null,
    val bufferingProgress: Int = 0,
    val playbackSpeed: Float = 1f
)

class PlayerViewModel(private val context: Context) : ViewModel() {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    private val mediaController = MediaController(exoPlayer, context)
    private val torrservClient = TorrservClient(context)
    private val preferencesManager = PreferencesManager(context)
    private val codecManager = CodecManager()

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState

    private val _torrservStreams = MutableStateFlow<List<TorrservStream>>(emptyList())
    val torrservStreams: StateFlow<List<TorrservStream>> = _torrservStreams

    init {
        setupPlayerListener()
        loadPreferences()
    }

    private fun setupPlayerListener() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                updateUiState()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateUiState()
            }

            override fun onPositionDiscontinuity(oldPos: Player.PositionInfo, newPos: Player.PositionInfo, reason: Int) {
                updateUiState()
            }
        })
    }

    private fun updateUiState() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            isPlaying = exoPlayer.isPlaying,
            currentPosition = exoPlayer.currentPosition,
            duration = exoPlayer.duration,
            bufferedPosition = exoPlayer.bufferedPosition,
            bufferingProgress = when (exoPlayer.playbackState) {
                Player.STATE_BUFFERING -> ((exoPlayer.bufferedPosition.toFloat() / exoPlayer.duration.toFloat()) * 100).toInt().coerceIn(0, 100)
                else -> 100
            }
        )
    }

    fun playMediaUrl(url: String, title: String = "Playing") {
        viewModelScope.launch {
            try {
                val mediaItem = MediaItem.fromUri(url)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.play()
                
                _uiState.value = _uiState.value.copy(
                    currentTitle = title,
                    errorMessage = null
                )
                
                Timber.d("Playing: $title from $url")
            } catch (e: Exception) {
                Timber.e(e, "Error playing media")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
        updateUiState()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs.coerceIn(0, exoPlayer.duration))
        updateUiState()
    }

    fun fastForward(seconds: Int = 10) {
        val newPosition = (exoPlayer.currentPosition + (seconds * 1000)).coerceAtMost(exoPlayer.duration)
        exoPlayer.seekTo(newPosition)
        updateUiState()
    }

    fun rewind(seconds: Int = 10) {
        val newPosition = (exoPlayer.currentPosition - (seconds * 1000)).coerceAtLeast(0)
        exoPlayer.seekTo(newPosition)
        updateUiState()
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed)
        _uiState.value = _uiState.value.copy(playbackSpeed = speed)
        viewModelScope.launch {
            preferencesManager.savePlaybackSpeed(speed)
        }
    }

    fun fetchTorrservStreams(query: String) {
        viewModelScope.launch {
            try {
                val streams = torrservClient.searchStreams(query)
                _torrservStreams.value = streams
                Timber.d("Fetched ${streams.size} streams")
            } catch (e: Exception) {
                Timber.e(e, "Error fetching streams")
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to fetch streams: ${e.message}"
                )
            }
        }
    }

    fun handleRemoteKeyEvent(keyCode: Int) {
        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_ENTER -> togglePlayPause()
            KeyEvent.KEYCODE_MEDIA_PLAY -> exoPlayer.play()
            KeyEvent.KEYCODE_MEDIA_PAUSE -> exoPlayer.pause()
            KeyEvent.KEYCODE_DPAD_RIGHT -> fastForward()
            KeyEvent.KEYCODE_DPAD_LEFT -> rewind()
            else -> {}
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            try {
                val savedSpeed = preferencesManager.getPlaybackSpeed()
                exoPlayer.setPlaybackSpeed(savedSpeed)
                _uiState.value = _uiState.value.copy(playbackSpeed = savedSpeed)
            } catch (e: Exception) {
                Timber.e(e, "Error loading preferences")
            }
        }
    }

    fun release() {
        exoPlayer.release()
    }
}

data class TorrservStream(
    val id: String,
    val name: String,
    val url: String,
    val posterUrl: String?,
    val quality: String,
    val seeds: Int,
    val peers: Int
)