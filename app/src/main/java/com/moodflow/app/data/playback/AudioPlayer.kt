package com.moodflow.app.data.playback

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.moodflow.app.data.db.TrackEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AudioPlayer(context: Context) {
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentTime = MutableStateFlow(0L)
    val currentTime: StateFlow<Long> = _currentTime

    private val _currentTrack = MutableStateFlow<TrackEntity?>(null)
    val currentTrack: StateFlow<TrackEntity?> = _currentTrack

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    init {
        mediaPlayer.setOnCompletionListener {
            _isPlaying.value = false
            onTrackCompleted?.invoke()
        }

        mediaPlayer.setOnErrorListener { _, what, extra ->
            _isPlaying.value = false
            onError?.invoke("Error: $what, $extra")
            true
        }
    }

    var onTrackCompleted: (() -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    fun playTrack(track: TrackEntity) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(track.audioPath ?: return)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                _isPlaying.value = true
                _currentTrack.value = track
                _duration.value = mediaPlayer.duration.toLong()
                startProgressUpdate()
            }
        } catch (e: Exception) {
            onError?.invoke("Failed to play track: ${e.message}")
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _isPlaying.value = false
        }
    }

    fun resume() {
        if (!mediaPlayer.isPlaying && _currentTrack.value != null) {
            mediaPlayer.start()
            _isPlaying.value = true
            startProgressUpdate()
        }
    }

    fun stop() {
        mediaPlayer.stop()
        _isPlaying.value = false
        _currentTime.value = 0L
        handler.removeCallbacksAndMessages(null)
    }

    fun seekTo(position: Long) {
        mediaPlayer.seekTo(position.toInt())
    }

    fun release() {
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun startProgressUpdate() {
        handler.post(object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    _currentTime.value = mediaPlayer.currentPosition.toLong()
                    handler.postDelayed(this, 500) // Update every 500ms
                }
            }
        })
    }

    fun getDuration(): Long = mediaPlayer.duration.toLong()

    fun getCurrentPosition(): Long = mediaPlayer.currentPosition.toLong()
}
