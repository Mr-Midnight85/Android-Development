package com.moodflow.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "moods",
    indices = [androidx.room.Index(value = ["id"], unique = true)]
)
data class MoodEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val description: String,
    val color: String,
    val isCustom: Boolean = false
)

@Entity(
    tableName = "tracks",
    indices = [androidx.room.Index(value = ["id"], unique = true)]
)
data class TrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val source: String, // "local" or "spotify"
    val audioPath: String? = null, // For local files
    val spotifyUri: String? = null, // For Spotify tracks
    val addedDate: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "listening_events",
    foreignKeys = [
        ForeignKey(
            entity = TrackEntity::class,
            parentColumns = ["id"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["moodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["trackId", "moodId"]),
        androidx.room.Index(value = ["timestamp"])
    ]
)
data class ListeningEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trackId: String,
    val moodId: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val durationPlayed: Long, // Milliseconds user listened
    val userAction: String, // "completed", "skipped", "repeated"
    val sessionId: String = "" // For grouping listening sessions
)

@Entity(
    tableName = "track_mood_tags",
    primaryKeys = ["trackId", "moodId"],
    foreignKeys = [
        ForeignKey(
            entity = TrackEntity::class,
            parentColumns = ["id"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["moodId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TrackMoodTagEntity(
    val trackId: String,
    val moodId: Int,
    val weight: Float = 1.0f, // Score for recommendation weighting
    val taggedAt: Long = System.currentTimeMillis()
)
