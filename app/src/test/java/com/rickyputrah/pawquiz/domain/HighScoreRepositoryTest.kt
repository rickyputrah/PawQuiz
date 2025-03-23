package com.rickyputrah.pawquiz.domain

import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

class HighScoreRepositoryTest {

    private val preferences = SPMockBuilder().createSharedPreferences()
    private val repository by lazy(LazyThreadSafetyMode.NONE) {
        HighScoreRepositoryImpl(
            preferences = preferences
        )
    }

    @Test
    fun `Given initialized first time When getHighScore Then return 0`() {
        assertEquals(0, repository.getHighScore().value)
    }

    @Test
    fun `Given user save score lower than previous high score When get Then return previous high score`() {
        repository.saveHighScore(2)

        repository.saveHighScore(1)

        assertEquals(2, repository.getHighScore().value)
    }

    @Test
    fun `Given user save score higher than previous high score When get Then return new high score`() {
        repository.saveHighScore(2)

        repository.saveHighScore(4)

        assertEquals(4, repository.getHighScore().value)
    }

    @Test
    fun `Given user save new high score When get Then return new score`() {

        repository.saveHighScore(2)

        assertEquals(2, repository.getHighScore().value)
    }
}