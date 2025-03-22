package com.rickyputrah.pawquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rickyputrah.pawquiz.ui.PawQuizApp
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawQuizTheme {
                PawQuizApp()
            }
        }
    }
}