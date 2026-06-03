package com.moodflow.app.data.playback

import com.moodflow.app.data.db.ListeningEventDao
import com.moodflow.app.data.db.ListeningEventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class ListeningEventLogger(private val eventDao: ListeningEventDao) {

    private val sessionId = UUID.randomUUID().toString()

    suspend fun logPlayEvent(
        trackId: String,
        moodId: Int,
        durationPlayed: Long
    ) = withContext(Dispatchers.IO) {
        val event = ListeningEventEntity(
            trackId = trackId,
            moodId = moodId,
            durationPlayed = durationPlayed,
            userAction = "completed",
            sessionId = sessionId
        )
        eventDao.insertEvent(event)
    }

    suspend fun logSkipEvent(
        trackId: String,
        moodId: Int,
        durationPlayed: Long
    ) = withContext(Dispatchers.IO) {
        val event = ListeningEventEntity(
            trackId = trackId,
            moodId = moodId,
            durationPlayed = durationPlayed,
            userAction = "skipped",
            sessionId = sessionId
        )
        eventDao.insertEvent(event)
    }

    suspend fun logRepeatEvent(
        trackId: String,
        moodId: Int
    ) = withContext(Dispatchers.IO) {
        val event = ListeningEventEntity(
            trackId = trackId,
            moodId = moodId,
            durationPlayed = 0L,
            userAction = "repeated",
            sessionId = sessionId
        )
        eventDao.insertEvent(event)
    }

    suspend fun getRecommendations(moodId: Int, limit: Int = 10): List<String> =
        withContext(Dispatchers.IO) {
            eventDao.getTracksPlayedInMood(moodId).take(limit)
        }

    suspend fun getMoodStats(moodId: Int): Pair<Long, Long> = withContext(Dispatchers.IO) {
        val completedCount = eventDao.getCompletedCountForMood(moodId)
        val recentEvents = eventDao.getEventsByMood(moodId, limit = 50)
        completedCount to recentEvents.size.toLong()
    }
}
