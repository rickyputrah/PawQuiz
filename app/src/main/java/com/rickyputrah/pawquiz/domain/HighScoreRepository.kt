package com.rickyputrah.pawquiz.domain

import android.content.SharedPreferences
import androidx.core.content.edit
import com.rickyputrah.pawquiz.di.HighScoreModule.Companion.HIGH_SCORE_PREFS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Named

interface HighScoreRepository {
    fun getHighScore(): StateFlow<Int>

    fun saveHighScore(highScore: Int)
}

class HighScoreRepositoryImpl @Inject constructor(
    @Named(HIGH_SCORE_PREFS) private val preferences: SharedPreferences
) : HighScoreRepository {
    private val _scoreFlow = MutableStateFlow(getCurrentHighScore())

    override fun getHighScore(): StateFlow<Int> = _scoreFlow.asStateFlow()

    override fun saveHighScore(score: Int) {
        if (score > getCurrentHighScore()) {
            _scoreFlow.tryEmit(score)
            preferences.edit {
                putInt(HIGH_SCORE_KEY, score)
            }
        }
    }

    private fun getCurrentHighScore(): Int {
        return preferences.getInt(HIGH_SCORE_KEY, 0)
    }

    companion object {
        private const val HIGH_SCORE_KEY = "high_score_key"
    }
}