package com.moodflow.app.data.playback

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Wrapper around ExoPlayer for Mood Flow playback management
 */
class MoodFlowMediaPlayer(context: Context) {
    
    private val exoPlayer = ExoPlayer.Builder(context).build()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition
    
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration
    
    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle
    
    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode
    
    enum class RepeatMode {
        OFF, ALL, ONE
    }
    
    init {
        setupPlayerListener()
    }
    
    private fun setupPlayerListener() {
        exoPlayer.addListener(object : androidx.media3.common.Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                _isPlaying.value = playing
            }
            
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    ExoPlayer.STATE_BUFFERING -> {}
                    ExoPlayer.STATE_ENDED -> {
                        // Auto-advance based on repeat mode
                        when (_repeatMode.value) {
                            RepeatMode.ONE -> exoPlayer.seekTo(0)
                            RepeatMode.ALL -> {
                                if (exoPlayer.hasNextMediaItem()) {
                                    exoPlayer.seekToNextMediaItem()
                                } else {
                                    exoPlayer.seekTo(0)
                                    if (_isShuffle.value) {
                                        exoPlayer.shuffleModeEnabled = true
                                    }
                                }
                            }
                            RepeatMode.OFF -> {
                                if (exoPlayer.hasNextMediaItem()) {
                                    exoPlayer.seekToNextMediaItem()
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
            
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _duration.value = exoPlayer.duration
            }
        })
    }
    
    fun loadPlaylist(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems, 0, 0)
        exoPlayer.prepare()
    }
    
    fun addToPlaylist(mediaItem: MediaItem) {
        exoPlayer.addMediaItem(mediaItem)
    }
    
    fun playTrack(trackPath: String) {
        val mediaItem = MediaItem.fromUri(trackPath)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }
    
    fun play() {
        exoPlayer.play()
    }
    
    fun pause() {
        exoPlayer.pause()
    }
    
    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }
    
    fun next() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNextMediaItem()
        }
    }
    
    fun previous() {
        if (exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPreviousMediaItem()
        }
    }
    
    fun setShuffle(enabled: Boolean) {
        _isShuffle.value = enabled
        exoPlayer.shuffleModeEnabled = enabled
    }
    
    fun setRepeatMode(mode: RepeatMode) {
        _repeatMode.value = mode
        exoPlayer.repeatMode = when (mode) {
            RepeatMode.OFF -> ExoPlayer.REPEAT_MODE_OFF
            RepeatMode.ALL -> ExoPlayer.REPEAT_MODE_ALL
            RepeatMode.ONE -> ExoPlayer.REPEAT_MODE_ONE
        }
    }
    
    fun getCurrentPosition(): Long = exoPlayer.currentPosition
    
    fun getDuration(): Long = exoPlayer.duration
    
    fun getProgress(): Float {
        val duration = exoPlayer.duration
        return if (duration > 0) {
            (exoPlayer.currentPosition.toFloat() / duration).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
    
    fun release() {
        exoPlayer.release()
    }
}
