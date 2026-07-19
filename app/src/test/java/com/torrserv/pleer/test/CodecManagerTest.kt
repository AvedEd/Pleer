package com.torrserv.pleer.test

import com.torrserv.pleer.player.CodecManager
import com.torrserv.pleer.player.MediaInfo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CodecManagerTest {

    private lateinit var codecManager: CodecManager

    @Before
    fun setup() {
        codecManager = CodecManager()
    }

    @Test
    fun testH264CodecSupport() {
        assertTrue(codecManager.isSupportedCodec("h264"))
        assertTrue(codecManager.isSupportedCodec("H.264"))
    }

    @Test
    fun testHEVCCodecSupport() {
        assertTrue(codecManager.isSupportedCodec("hevc"))
        assertTrue(codecManager.isSupportedCodec("h.265"))
    }

    @Test
    fun testVP9CodecSupport() {
        assertTrue(codecManager.isSupportedCodec("vp9"))
    }

    @Test
    fun testAudioCodecSupport() {
        assertTrue(codecManager.isSupportedCodec("aac"))
        assertTrue(codecManager.isSupportedCodec("mp3"))
        assertTrue(codecManager.isSupportedCodec("flac"))
        assertTrue(codecManager.isSupportedCodec("opus"))
    }

    @Test
    fun testContainerSupport() {
        assertTrue(codecManager.isSupportedContainer("mp4"))
        assertTrue(codecManager.isSupportedContainer("mkv"))
        assertTrue(codecManager.isSupportedContainer("webm"))
        assertTrue(codecManager.isSupportedContainer("m3u8"))
    }

    @Test
    fun testMediaUrlAnalysis() {
        val mediaInfo: MediaInfo = codecManager.analyzeMediaUrl("https://example.com/video.mp4")
        assertEquals("mp4", mediaInfo.extension)
        assertTrue(mediaInfo.isSupported)
        assertFalse(mediaInfo.isLiveStream)
    }

    @Test
    fun testLiveStreamDetection() {
        val liveInfo: MediaInfo = codecManager.analyzeMediaUrl("https://example.com/stream.m3u8")
        assertTrue(liveInfo.isLiveStream)
        assertTrue(liveInfo.isSupported)
    }

    @Test
    fun testUnsupportedCodec() {
        assertFalse(codecManager.isSupportedCodec("unsupported_codec"))
    }

    @Test
    fun testCodecListGeneration() {
        val codecs = codecManager.getSupportedCodecsInfo()
        assertTrue(codecs.videoCodecs.isNotEmpty())
        assertTrue(codecs.audioCodecs.isNotEmpty())
        assertTrue(codecs.containers.isNotEmpty())
    }
}
