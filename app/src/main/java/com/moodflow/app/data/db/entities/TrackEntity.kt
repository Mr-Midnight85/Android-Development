package com.moodflow.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val album: String = "",
    val duration: Long = 0L, // in milliseconds
    val genre: String = "",
    val tempo: Int = 0, // BPM
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
