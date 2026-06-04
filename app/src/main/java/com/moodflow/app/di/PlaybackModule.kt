package com.moodflow.app.di

import android.content.Context
import com.moodflow.app.data.playback.MoodFlowMediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaybackModule {
    
    @Provides
    @Singleton
    fun provideMoodFlowMediaPlayer(
        @ApplicationContext context: Context
    ): MoodFlowMediaPlayer {
        return MoodFlowMediaPlayer(context)
    }
}
