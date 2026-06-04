package com.moodflow.app.data.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.moodflow.app.data.db.TrackEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val mediaPlayer: MoodFlowMediaPlayer
) : ViewModel() {
    
    val isPlaying = mediaPlayer.isPlaying
    val isShuffle = mediaPlayer.isShuffle
    val repeatMode = mediaPlayer.repeatMode
    
    private val _currentTrack = MutableStateFlow<TrackEntity?>(null)
    val currentTrack: StateFlow<TrackEntity?> = _currentTrack
    
    private val _playlist = MutableStateFlow<List<TrackEntity>>(emptyList())
    val playlist: StateFlow<List<TrackEntity>> = _playlist
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition
    
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration
    
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress
    
    init {
        setupPositionUpdates()
    }
    
    private fun setupPositionUpdates() {
        viewModelScope.launch {
            while (true) {
                if (mediaPlayer.getCurrentPosition() >= 0) {
                    _currentPosition.value = mediaPlayer.getCurrentPosition()
                    _duration.value = mediaPlayer.getDuration()
                    _progress.value = mediaPlayer.getProgress()
                }
                kotlinx.coroutines.delay(500)
            }
        }
    }
    
    fun loadPlaylist(tracks: List<TrackEntity>) {
        _playlist.value = tracks
        val mediaItems = tracks.map { track ->
            val uri = if (track.source == "local") {
                "file://${track.audioPath}"
            } else {
                track.spotifyUri ?: ""
            }
            MediaItem.fromUri(uri)
        }
        mediaPlayer.loadPlaylist(mediaItems)
    }
    
    fun playTrack(track: TrackEntity) {
        _currentTrack.value = track
        val uri = if (track.source == "local") {
            "file://${track.audioPath}"
        } else {
            track.spotifyUri ?: ""
        }
        mediaPlayer.playTrack(uri)
    }
    
    fun playPause() {
        if (isPlaying.value) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.play()
        }
    }
    
    fun nextTrack() {
        mediaPlayer.next()
        updateCurrentTrack()
    }
    
    fun previousTrack() {
        mediaPlayer.previous()
        updateCurrentTrack()
    }
    
    fun setShuffle(enabled: Boolean) {
        mediaPlayer.setShuffle(enabled)
    }
    
    fun setRepeatMode(mode: MoodFlowMediaPlayer.RepeatMode) {
        mediaPlayer.setRepeatMode(mode)
    }
    
    fun seekTo(position: Float) {
        val duration = mediaPlayer.getDuration()
        if (duration > 0) {
            mediaPlayer.seekTo((position * duration).toLong())
        }
    }
    
    private fun updateCurrentTrack() {
        val currentIndex = mediaPlayer.exoPlayer.currentMediaItemIndex
        if (currentIndex in _playlist.value.indices) {
            _currentTrack.value = _playlist.value[currentIndex]
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}
