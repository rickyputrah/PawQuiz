package com.rickyputrah.pawquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.ImageLoader
import com.rickyputrah.pawquiz.ui.PawQuizApp
import com.rickyputrah.pawquiz.ui.theme.PawQuizTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawQuizTheme(imageLoader) {
                PawQuizApp()
            }
        }
    }
}