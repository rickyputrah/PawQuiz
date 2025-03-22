package com.rickyputrah.pawquiz.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader

val LocalImageLoader = staticCompositionLocalOf<ImageLoader> { error("No ImageLoader Provided") }

@Composable
fun getDefaultImageLoader() = ImageLoader(LocalContext.current)