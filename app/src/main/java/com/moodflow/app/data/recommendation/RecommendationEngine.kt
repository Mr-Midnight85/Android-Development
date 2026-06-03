package com.moodflow.app.data.recommendation

import com.moodflow.app.data.db.ListeningEventDao
import com.moodflow.app.data.db.TrackEntity
import com.moodflow.app.data.db.TrackMoodTagDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecommendationEngine(
    private val eventDao: ListeningEventDao,
    private val trackMoodTagDao: TrackMoodTagDao
) {

    suspend fun getRecommendations(
        moodId: Int,
        allTracks: List<TrackEntity>,
        limit: Int = 10
    ): List<TrackEntity> = withContext(Dispatchers.IO) {
        val recentTracksForMood = eventDao.getTracksPlayedInMood(moodId)
        val tracksForMoodByWeight = trackMoodTagDao.getTracksForMoodByWeight(moodId, limit = limit * 2)

        val recommended = mutableListOf<TrackEntity>()
        val seen = mutableSetOf<String>()

        for (trackId in recentTracksForMood.take(limit)) {
            val track = allTracks.find { it.id == trackId }
            if (track != null && !seen.contains(trackId)) {
                recommended.add(track)
                seen.add(trackId)
                if (recommended.size >= limit) break
            }
        }

        for (tag in tracksForMoodByWeight) {
            val track = allTracks.find { it.id == tag.trackId }
            if (track != null && !seen.contains(track.id)) {
                recommended.add(track)
                seen.add(track.id)
                if (recommended.size >= limit) break
            }
        }

        recommended.take(limit)
    }

    suspend fun scoreTrackForMood(
        trackId: String,
        moodId: Int,
        allTracks: List<TrackEntity>
    ): Float = withContext(Dispatchers.IO) {
        val events = eventDao.getEventsByTrackAndMood(trackId, moodId)

        var score = 0f

        val completedCount = events.count { it.userAction == "completed" }
        val skippedCount = events.count { it.userAction == "skipped" }

        score += completedCount * 2.0f
        score -= skippedCount * 1.0f

        val recentEvents = events.sortedByDescending { it.timestamp }.take(5)
        if (recentEvents.isNotEmpty()) {
            score += 1.0f
        }

        maxOf(0f, score)
    }

    suspend fun updateTrackMoodWeight(
        trackId: String,
        moodId: Int,
        weight: Float
    ) = withContext(Dispatchers.IO) {
        if (weight > 0f) {
            val tag = com.moodflow.app.data.db.TrackMoodTagEntity(
                trackId = trackId,
                moodId = moodId,
                weight = weight
            )
            trackMoodTagDao.tagTrackWithMood(tag)
        }
    }

    suspend fun getSimilarArtists(
        moodId: Int,
        allTracks: List<TrackEntity>,
        limit: Int = 5
    ): List<String> = withContext(Dispatchers.IO) {
        val tracksForMood = eventDao.getTracksPlayedInMood(moodId)
        val artistCounts = mutableMapOf<String, Int>()

        for (trackId in tracksForMood) {
            val track = allTracks.find { it.id == trackId } ?: continue
            artistCounts[track.artist] = (artistCounts[track.artist] ?: 0) + 1
        }

        artistCounts
            .entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key }
    }
}
