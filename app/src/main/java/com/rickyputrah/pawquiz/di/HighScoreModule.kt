package com.rickyputrah.pawquiz.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.rickyputrah.pawquiz.domain.HighScoreRepository
import com.rickyputrah.pawquiz.domain.HighScoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class HighScoreModule {
    @Binds
    @Singleton
    abstract fun bindHighScoreRepository(impl: HighScoreRepositoryImpl): HighScoreRepository

    companion object {
        @Provides
        @Singleton
        @Named(HIGH_SCORE_PREFS)
        fun provideHighScorePreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(KEY, MODE_PRIVATE)
        }

        private const val KEY = "high_score_key"
        const val HIGH_SCORE_PREFS = "high_score_prefs"
    }
}