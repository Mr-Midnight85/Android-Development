package com.moodflow.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        MoodEntity::class,
        TrackEntity::class,
        ListeningEventEntity::class,
        TrackMoodTagEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MoodFlowDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun moodDao(): MoodDao
    abstract fun listeningEventDao(): ListeningEventDao
    abstract fun trackMoodTagDao(): TrackMoodTagDao

    companion object {
        @Volatile
        private var INSTANCE: MoodFlowDatabase? = null

        fun getDatabase(context: Context): MoodFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodFlowDatabase::class.java,
                    "moodflow_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
