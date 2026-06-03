package com.moodflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.moodflow.app.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedMoodId by remember { mutableStateOf<Int?>(null) }
                    
                    HomeScreen(
                        selectedMoodId = selectedMoodId,
                        onMoodSelected = { moodId -> selectedMoodId = moodId },
                        onPlayRecommended = { /* TODO: Play recommended */ }
                    )
                }
            }
        }
    }
}

@Composable
fun MoodFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

