package com.questionpaper.repository

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.questionpaper.repository.ui.components.MainScreen
import com.questionpaper.repository.ui.theme.QuestionPaperRepositoryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuestionPaperRepositoryTheme {
                MainScreen()
            }
        }
    }
}
