package com.moodflow.app.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moodflow.app.data.db.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)
    
    @Update
    suspend fun updateTrack(track: TrackEntity)
    
    @Delete
    suspend fun deleteTrack(track: TrackEntity)
    
    @Query("DELETE FROM tracks WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: String)
    
    @Query("SELECT * FROM tracks WHERE id = :trackId")
    suspend fun getTrackById(trackId: String): TrackEntity?
    
    @Query("SELECT * FROM tracks")
    fun getAllTracks(): Flow<List<TrackEntity>>
    
    @Query("SELECT * FROM tracks WHERE artist = :artist")
    suspend fun getTracksByArtist(artist: String): List<TrackEntity>
    
    @Query("SELECT * FROM tracks WHERE genre = :genre")
    suspend fun getTracksByGenre(genre: String): List<TrackEntity>
    
    @Query("SELECT * FROM tracks WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    suspend fun searchTracks(query: String): List<TrackEntity>
    
    @Query("SELECT COUNT(*) FROM tracks")
    suspend fun getTotalTrackCount(): Int
}