package com.moodflow.app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Query("SELECT * FROM tracks WHERE id = :trackId")
    suspend fun getTrack(trackId: String): TrackEntity?

    @Query("SELECT * FROM tracks ORDER BY title ASC")
    fun getAllTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun searchTracks(query: String): Flow<List<TrackEntity>>

    @Query("SELECT COUNT(*) FROM tracks")
    fun getTrackCount(): Flow<Long>

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM tracks")
    suspend fun clearAllTracks()
}

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoods(moods: List<MoodEntity>)

    @Query("SELECT * FROM moods WHERE id = :moodId")
    suspend fun getMood(moodId: Int): MoodEntity?

    @Query("SELECT * FROM moods WHERE isCustom = 0 ORDER BY id ASC")
    suspend fun getDefaultMoods(): List<MoodEntity>

    @Query("SELECT * FROM moods ORDER BY id ASC")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Delete
    suspend fun deleteMood(mood: MoodEntity)
}

@Dao
interface ListeningEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: ListeningEventEntity)

    @Query("SELECT * FROM listening_events WHERE trackId = :trackId AND moodId = :moodId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getEventsByTrackAndMood(trackId: String, moodId: Int, limit: Int = 10): List<ListeningEventEntity>

    @Query("SELECT * FROM listening_events WHERE moodId = :moodId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getEventsByMood(moodId: Int, limit: Int = 50): List<ListeningEventEntity>

    @Query("SELECT * FROM listening_events ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentEvents(limit: Int = 100): Flow<List<ListeningEventEntity>>

    @Query("SELECT DISTINCT trackId FROM listening_events WHERE moodId = :moodId ORDER BY timestamp DESC")
    suspend fun getTracksPlayedInMood(moodId: Int): List<String>

    @Query("SELECT COUNT(*) FROM listening_events WHERE moodId = :moodId AND userAction = 'completed'")
    suspend fun getCompletedCountForMood(moodId: Int): Long

    @Delete
    suspend fun deleteEvent(event: ListeningEventEntity)

    @Query("DELETE FROM listening_events")
    suspend fun clearAllEvents()
}

@Dao
interface TrackMoodTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun tagTrackWithMood(tag: TrackMoodTagEntity)

    @Query("SELECT moodId FROM track_mood_tags WHERE trackId = :trackId")
    suspend fun getMoodsForTrack(trackId: String): List<Int>

    @Query("SELECT trackId, weight FROM track_mood_tags WHERE moodId = :moodId ORDER BY weight DESC LIMIT :limit")
    suspend fun getTracksForMoodByWeight(moodId: Int, limit: Int = 50): List<TrackMoodTagEntity>

    @Query("DELETE FROM track_mood_tags WHERE trackId = :trackId AND moodId = :moodId")
    suspend fun removeTag(trackId: String, moodId: Int)

    @Query("DELETE FROM track_mood_tags")
    suspend fun clearAllTags()
}
