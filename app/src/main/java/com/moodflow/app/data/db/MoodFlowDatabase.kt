package com.moodflow.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moodflow.app.data.db.daos.ListeningEventDao
import com.moodflow.app.data.db.daos.MoodDao
import com.moodflow.app.data.db.daos.TrackDao
import com.moodflow.app.data.db.daos.TrackMoodTagDao
import com.moodflow.app.data.db.entities.ListeningEventEntity
import com.moodflow.app.data.db.entities.MoodEntity
import com.moodflow.app.data.db.entities.TrackEntity
import com.moodflow.app.data.db.entities.TrackMoodTagEntity

@Database(
    entities = [
        TrackEntity::class,
        MoodEntity::class,
        ListeningEventEntity::class,
        TrackMoodTagEntity::class
    ],
    version = 1,
    exportSchema = true
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
                    .fallbackToDestructiveMigration() // For development - remove in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}