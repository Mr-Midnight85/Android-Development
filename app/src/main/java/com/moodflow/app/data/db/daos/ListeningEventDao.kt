package com.moodflow.app.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moodflow.app.data.db.entities.ListeningEventEntity

@Dao
interface ListeningEventDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: ListeningEventEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<ListeningEventEntity>)
    
    @Query("SELECT * FROM listening_events WHERE id = :eventId")
    suspend fun getEventById(eventId: Long): ListeningEventEntity?
    
    @Query("SELECT * FROM listening_events WHERE trackId = :trackId AND moodId = :moodId")
    suspend fun getEventsByTrackAndMood(trackId: String, moodId: Int): List<ListeningEventEntity>
    
    @Query("SELECT * FROM listening_events WHERE moodId = :moodId ORDER BY timestamp DESC")
    suspend fun getEventsByMood(moodId: Int): List<ListeningEventEntity>
    
    @Query("SELECT trackId FROM listening_events WHERE moodId = :moodId GROUP BY trackId ORDER BY COUNT(*) DESC")
    suspend fun getTracksPlayedInMood(moodId: Int): List<String>
    
    @Query("SELECT * FROM listening_events WHERE userAction = :action ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getEventsByAction(action: String, limit: Int = 50): List<ListeningEventEntity>
    
    @Query("SELECT COUNT(*) FROM listening_events WHERE trackId = :trackId AND userAction = 'completed'")
    suspend fun getCompletedCountForTrack(trackId: String): Int
    
    @Query("SELECT COUNT(*) FROM listening_events WHERE trackId = :trackId AND userAction = 'skipped'")
    suspend fun getSkippedCountForTrack(trackId: String): Int
    
    @Query("SELECT * FROM listening_events WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    suspend fun getEventsInTimeRange(startTime: Long, endTime: Long): List<ListeningEventEntity>
    
    @Query("DELETE FROM listening_events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: Long)
    
    @Query("SELECT COUNT(*) FROM listening_events")
    suspend fun getTotalEventCount(): Int
}