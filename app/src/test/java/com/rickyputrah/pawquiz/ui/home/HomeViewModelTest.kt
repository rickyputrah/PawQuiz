package com.rickyputrah.pawquiz.ui.home

import com.rickyputrah.pawquiz.domain.HighScoreRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val highScoreRepository: HighScoreRepository = mockk(relaxed = true)
    private val highScoreFlow = MutableStateFlow(0)

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        HomeViewModel(
            highScoreRepository = highScoreRepository,
            ioDispatcher = UnconfinedTestDispatcher(),
            mainDispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Before
    fun setup() {
        every { highScoreRepository.getHighScore() } returns highScoreFlow
    }

    @Test
    fun `When init Then return score from repository`() {
        assertEquals(0, viewModel.score.value)
    }

    @Test
    fun `When value got updated from repository Then emits new value`() {

        assertEquals(0, viewModel.score.value)

        highScoreFlow.tryEmit(10)

        assertEquals(10, viewModel.score.value)
    }
}