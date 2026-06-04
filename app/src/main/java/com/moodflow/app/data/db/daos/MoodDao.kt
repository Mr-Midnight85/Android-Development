package com.moodflow.app.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moodflow.app.data.db.entities.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoods(moods: List<MoodEntity>)
    
    @Update
    suspend fun updateMood(mood: MoodEntity)
    
    @Delete
    suspend fun deleteMood(mood: MoodEntity)
    
    @Query("DELETE FROM moods WHERE id = :moodId")
    suspend fun deleteMoodById(moodId: Int)
    
    @Query("SELECT * FROM moods WHERE id = :moodId")
    suspend fun getMoodById(moodId: Int): MoodEntity?
    
    @Query("SELECT * FROM moods")
    fun getAllMoods(): Flow<List<MoodEntity>>
    
    @Query("SELECT * FROM moods WHERE name = :moodName")
    suspend fun getMoodByName(moodName: String): MoodEntity?
    
    @Query("SELECT COUNT(*) FROM moods")
    suspend fun getTotalMoodCount(): Int
}