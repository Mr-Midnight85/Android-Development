package com.moodflow.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

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
    ],
    indices = [
        Index(value = ["moodId"]),
        Index(value = ["weight"])
    ]
)
data class TrackMoodTagEntity(
    val trackId: String, // Foreign key to TrackEntity
    val moodId: Int, // Foreign key to MoodEntity
    val weight: Float = 0f // Weight/score for how well track matches mood (0-1 or higher)
)