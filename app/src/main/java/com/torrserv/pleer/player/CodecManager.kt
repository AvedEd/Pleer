package com.torrserv.pleer.player

import timber.log.Timber

class CodecManager {

    private val supportedVideoCodecs = setOf(
        "h264", "h.264", "avc",
        "hevc", "h.265", "hvc1",
        "vp8", "vp80",
        "vp9", "vp90",
        "av1"
    )

    private val supportedAudioCodecs = setOf(
        "aac", "mp4a",
        "mp3",
        "flac",
        "opus",
        "vorbis", "vorb",
        "ac3", "eac3",
        "dts"
    )

    private val supportedContainers = setOf(
        "mp4", "mkv", "webm", "avi", "mov", "flv", "ts", "m3u8", "mpd"
    )

    fun isSupportedCodec(codecName: String): Boolean {
        val normalizedCodec = codecName.lowercase().replace(".", "")
        return supportedVideoCodecs.contains(normalizedCodec) || 
               supportedAudioCodecs.contains(normalizedCodec)
    }

    fun isSupportedContainer(containerName: String): Boolean {
        return supportedContainers.contains(containerName.lowercase())
    }

    fun analyzeMediaUrl(url: String): MediaInfo {
        val extension = url.substringAfterLast(".").lowercase()
        val isLiveStream = url.contains("m3u8") || url.contains("mpd") || url.contains("hls")
        
        return MediaInfo(
            url = url,
            extension = extension,
            isLiveStream = isLiveStream,
            isSupported = isSupportedContainer(extension) || isLiveStream
        )
    }

    fun getSupportedCodecsInfo(): CodecsInfo {
        return CodecsInfo(
            videoCodecs = supportedVideoCodecs.toList(),
            audioCodecs = supportedAudioCodecs.toList(),
            containers = supportedContainers.toList()
        )
    }
}

data class MediaInfo(
    val url: String,
    val extension: String,
    val isLiveStream: Boolean,
    val isSupported: Boolean
)

data class CodecsInfo(
    val videoCodecs: List<String>,
    val audioCodecs: List<String>,
    val containers: List<String>
)