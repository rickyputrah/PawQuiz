package com.rickyputrah.pawquiz.di

import android.content.Context
import coil3.ImageLoader
import coil3.request.crossfade
import com.rickyputrah.pawquiz.data.ImagePreloader
import com.rickyputrah.pawquiz.data.ImagePreloaderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoilModule {

    @Binds
    @Singleton
    abstract fun bindsImagePreloader(impl: ImagePreloaderImpl): ImagePreloader

    companion object {
        @Provides
        @Singleton
        fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
            return ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }
    }
}