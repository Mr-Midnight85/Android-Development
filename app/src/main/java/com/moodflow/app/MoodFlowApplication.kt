package com.moodflow.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoodFlowApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize app-wide dependencies or logging here if needed
    }
}
