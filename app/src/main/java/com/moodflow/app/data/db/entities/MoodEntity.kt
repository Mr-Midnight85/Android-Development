package com.moodflow.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moods")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String, // "Happy", "Sad", "Energetic", "Calm", etc.
    val color: String = "#000000", // Hex color for UI
    val emoji: String = "🎵", // Emoji representation
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
