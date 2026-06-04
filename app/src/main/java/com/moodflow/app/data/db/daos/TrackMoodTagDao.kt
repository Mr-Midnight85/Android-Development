package com.moodflow.app.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moodflow.app.data.db.entities.TrackMoodTagEntity

@Dao
interface TrackMoodTagDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun tagTrackWithMood(tag: TrackMoodTagEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun tagTracksWithMoods(tags: List<TrackMoodTagEntity>)
    
    @Update
    suspend fun updateTag(tag: TrackMoodTagEntity)
    
    @Query("SELECT * FROM track_mood_tags WHERE trackId = :trackId AND moodId = :moodId")
    suspend fun getTagByTrackAndMood(trackId: String, moodId: Int): TrackMoodTagEntity?
    
    @Query("SELECT * FROM track_mood_tags WHERE moodId = :moodId ORDER BY weight DESC LIMIT :limit")
    suspend fun getTracksForMoodByWeight(moodId: Int, limit: Int = 10): List<TrackMoodTagEntity>
    
    @Query("SELECT * FROM track_mood_tags WHERE trackId = :trackId ORDER BY weight DESC")
    suspend fun getMoodsForTrack(trackId: String): List<TrackMoodTagEntity>
    
    @Query("SELECT * FROM track_mood_tags WHERE moodId = :moodId")
    suspend fun getAllTagsForMood(moodId: Int): List<TrackMoodTagEntity>
    
    @Query("SELECT AVG(weight) FROM track_mood_tags WHERE moodId = :moodId")
    suspend fun getAverageWeightForMood(moodId: Int): Float?
    
    @Query("SELECT MAX(weight) FROM track_mood_tags WHERE moodId = :moodId")
    suspend fun getMaxWeightForMood(moodId: Int): Float?
    
    @Query("DELETE FROM track_mood_tags WHERE trackId = :trackId AND moodId = :moodId")
    suspend fun removeTag(trackId: String, moodId: Int)
    
    @Query("DELETE FROM track_mood_tags WHERE trackId = :trackId")
    suspend fun removeAllTagsForTrack(trackId: String)
    
    @Query("SELECT COUNT(*) FROM track_mood_tags")
    suspend fun getTotalTagCount(): Int
}