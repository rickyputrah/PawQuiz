package com.rickyputrah.pawquiz.data

import android.content.Context
import coil3.ImageLoader
import coil3.request.ImageRequest
import com.rickyputrah.pawquiz.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

interface ImagePreloader {
    fun preloadImage(url: String)
}

class ImagePreloaderImpl @Inject constructor(
    private val imageLoader: ImageLoader,
    @ApplicationContext val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ImagePreloader {
    val scope = CoroutineScope(ioDispatcher)
    override fun preloadImage(url: String) {
        scope.launch {
            val request = ImageRequest.Builder(context)
                .data(url)
                .listener(
                    onSuccess = { _, _ ->
                        Timber.d("ImagePreloader - Successfully preloaded: $url")
                    },
                    onError = { _, error ->
                        Timber.e("ImagePreloader - Failed to preload: $url, error: ${error.throwable}")
                    }
                )
                .build()
            imageLoader.enqueue(request)
        }
    }
}