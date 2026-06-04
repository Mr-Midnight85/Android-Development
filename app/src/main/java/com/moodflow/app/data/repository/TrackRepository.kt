package com.moodflow.app.data.repository

import com.moodflow.app.data.db.*
import com.moodflow.app.data.recommendation.RecommendationEngine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackRepository @Inject constructor(
    private val trackDao: TrackDao,
    private val recommendationEngine: RecommendationEngine,
    private val listeningEventDao: ListeningEventDao,
    private val trackMoodTagDao: TrackMoodTagDao
) {
    
    fun getAllTracks(): Flow<List<TrackEntity>> = trackDao.getAllTracks()
    
    fun searchTracks(query: String): Flow<List<TrackEntity>> = trackDao.searchTracks(query)
    
    suspend fun insertTrack(track: TrackEntity) = trackDao.insertTrack(track)
    
    suspend fun insertTracks(tracks: List<TrackEntity>) = trackDao.insertTracks(tracks)
    
    suspend fun getTrack(trackId: String): TrackEntity? = trackDao.getTrack(trackId)
    
    suspend fun deleteTrack(track: TrackEntity) = trackDao.deleteTrack(track)
    
    suspend fun getRecommendationsForMood(moodId: Int, limit: Int = 10): List<TrackEntity> {
        return recommendationEngine.getRecommendationsForMood(moodId, limit)
    }
    
    /**
     * Log a listening event when user plays a track in a specific mood
     */
    suspend fun logListeningEvent(
        trackId: String,
        moodId: Int,
        durationPlayed: Long,
        userAction: String,
        sessionId: String = ""
    ) {
        val event = ListeningEventEntity(
            trackId = trackId,
            moodId = moodId,
            durationPlayed = durationPlayed,
            userAction = userAction,
            sessionId = sessionId
        )
        listeningEventDao.insertEvent(event)
        
        // Update track-mood weight for better recommendations
        val currentTag = trackMoodTagDao.getMoodsForTrack(trackId)
        val weight = when (userAction) {
            "completed" -> 2.0f  // Higher weight for completed plays
            "repeated" -> 1.5f   // Medium weight for repeats
            "skipped" -> 0.5f    // Lower weight for skips
            else -> 1.0f
        }
        
        trackMoodTagDao.tagTrackWithMood(
            TrackMoodTagEntity(trackId = trackId, moodId = moodId, weight = weight)
        )
    }
    
    fun getRecentEvents(limit: Int = 100): Flow<List<ListeningEventEntity>> {
        return listeningEventDao.getRecentEvents(limit)
    }
    
    suspend fun getTracksPlayedInMood(moodId: Int): List<String> {
        return listeningEventDao.getTracksPlayedInMood(moodId)
    }
}

class MoodRepository @Inject constructor(
    private val moodDao: MoodDao
) {
    
    fun getAllMoods(): Flow<List<MoodEntity>> = moodDao.getAllMoods()
    
    suspend fun getDefaultMoods(): List<MoodEntity> = moodDao.getDefaultMoods()
    
    suspend fun getMood(moodId: Int): MoodEntity? = moodDao.getMood(moodId)
    
    suspend fun insertMood(mood: MoodEntity) = moodDao.insertMood(mood)
    
    suspend fun insertMoods(moods: List<MoodEntity>) = moodDao.insertMoods(moods)
    
    suspend fun deleteMood(mood: MoodEntity) = moodDao.deleteMood(mood)
}
