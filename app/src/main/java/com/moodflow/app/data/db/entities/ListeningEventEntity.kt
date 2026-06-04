package com.moodflow.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    ]
)
data class ListeningEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trackId: String, // Foreign key to TrackEntity
    val moodId: Int, // Foreign key to MoodEntity
    val userAction: String = "started", // "started", "completed", "skipped", "paused"
    val playbackDuration: Long = 0L, // How long user listened in milliseconds
    val timestamp: Long = System.currentTimeMillis()
)
