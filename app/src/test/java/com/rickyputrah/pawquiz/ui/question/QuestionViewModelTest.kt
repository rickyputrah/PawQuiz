package com.rickyputrah.pawquiz.ui.question

import com.rickyputrah.pawquiz.domain.DogBreed
import com.rickyputrah.pawquiz.domain.GenerateQuestionUseCase
import com.rickyputrah.pawquiz.domain.HighScoreRepository
import com.rickyputrah.pawquiz.domain.Question
import com.rickyputrah.pawquiz.domain.QuestionException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class QuestionViewModelTest {

    private val generateQuestionUseCase: GenerateQuestionUseCase = mockk(relaxed = true)
    private val highScoreRepository: HighScoreRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val highScoreFlow = MutableStateFlow(0)

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        QuestionViewModel(
            highScoreRepository = highScoreRepository,
            generateQuestionUseCase = generateQuestionUseCase,
            ioDispatcher = testDispatcher,
            mainDispatcher = testDispatcher,
        )
    }

    @Before
    fun setup() {
        every { highScoreRepository.getHighScore() } returns highScoreFlow
    }

    @Test
    fun `Given generate question success When initialized Then state should have question and not loading`() =
        runTest {
            val expectedQuestion = Question(
                options = listOf(AIREDALE, AKITA, KELPIE_AUSTRALIAN, SHEPHERD_AUSTRALIAN),
                correctOption = KELPIE_AUSTRALIAN,
                imageUrl = "imageUrl"
            )
            coEvery { generateQuestionUseCase.invoke(4) } returns Result.success(expectedQuestion)

            viewModel
            testDispatcher.scheduler.advanceUntilIdle()

            val currentState = viewModel.uiState.value
            assertEquals(expectedQuestion, currentState.question)
            assertFalse(currentState.isLoading)
        }

    @Test
    fun `Given generate question fail and throw an exception When initialized Then state should have null question and not loading`() =
        runTest {
            coEvery { generateQuestionUseCase.invoke(4) } returns Result.failure(QuestionException.FailedToFetchDogBreedList())

            viewModel
            testDispatcher.scheduler.advanceUntilIdle()

            val currentState = viewModel.uiState.value
            assertNull(currentState.question)
            assertFalse(currentState.isLoading)
        }

    @Test
    fun `Given question answered correctly When onQuestionAnswered is called Then success state should be true and generate next question`() =
        runTest {
            val firstQuestion = Question(
                options = listOf(AIREDALE, AKITA, KELPIE_AUSTRALIAN, SHEPHERD_AUSTRALIAN),
                correctOption = KELPIE_AUSTRALIAN,
                imageUrl = "imageUrl"
            )
            val secondQuestion = Question(
                options = listOf(GREAT_DANE, SHEPHERD_AUSTRALIAN, KELPIE_AUSTRALIAN, AKITA),
                correctOption = GREAT_DANE,
                imageUrl = "imageUrl"
            )

            coEvery { generateQuestionUseCase.invoke(4) } returnsMany listOf(
                Result.success(firstQuestion),
                Result.success(secondQuestion)
            )

            viewModel
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(0, viewModel.uiState.value.currentScore)

            // When
            viewModel.onQuestionAnswered(firstQuestion, KELPIE_AUSTRALIAN)

            assertTrue(viewModel.uiState.value.isSuccess)
            assertEquals(1, viewModel.uiState.value.currentScore)
            verify(exactly = 0) { highScoreRepository.saveHighScore(1) }

            // Then advance until next question generated
            testDispatcher.scheduler.advanceTimeBy(2_001)

            // Then next question generated
            val finalState = viewModel.uiState.value
            assertEquals(secondQuestion, finalState.question)
            assertFalse(finalState.isSuccess)
        }

    @Test
    fun `Given question answered wrongly When new high score created Then set new high score state as true`() =
        runTest {
            val firstQuestion = Question(
                options = listOf(AIREDALE, AKITA, KELPIE_AUSTRALIAN, SHEPHERD_AUSTRALIAN),
                correctOption = KELPIE_AUSTRALIAN,
                imageUrl = "imageUrl"
            )
            val secondQuestion = Question(
                options = listOf(GREAT_DANE, SHEPHERD_AUSTRALIAN, KELPIE_AUSTRALIAN, AKITA),
                correctOption = GREAT_DANE,
                imageUrl = "imageUrl"
            )

            coEvery { generateQuestionUseCase.invoke(4) } returnsMany listOf(
                Result.success(firstQuestion),
                Result.success(secondQuestion)
            )

            viewModel
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(0, viewModel.uiState.value.currentScore)

            // When
            viewModel.onQuestionAnswered(firstQuestion, KELPIE_AUSTRALIAN)

            assertTrue(viewModel.uiState.value.isSuccess)
            assertEquals(1, viewModel.uiState.value.currentScore)
            verify(exactly = 0) { highScoreRepository.saveHighScore(1) }

            // Then advance until next question generated
            testDispatcher.scheduler.advanceTimeBy(2_001)

            // Then next question generated
            val finalState = viewModel.uiState.value
            assertEquals(secondQuestion, finalState.question)
            assertFalse(finalState.isSuccess)

            // When answered wrongly
            viewModel.onQuestionAnswered(secondQuestion, KELPIE_AUSTRALIAN)

            val currentState = viewModel.uiState.value
            assertTrue(currentState.isWrongAnswer)
            assertFalse(currentState.isSuccess)
            assertEquals(1, currentState.currentScore)
            verify { highScoreRepository.saveHighScore(1) }
            assertTrue(currentState.isNewHighScore)
        }

    @Test
    fun `Given question answered wrong When onQuestionAnswered is called Then wrong answer state should be true`() =
        runTest {
            val expectedQuestion = Question(
                options = listOf(AIREDALE, AKITA, KELPIE_AUSTRALIAN, SHEPHERD_AUSTRALIAN),
                correctOption = KELPIE_AUSTRALIAN,
                imageUrl = "imageUrl"
            )
            coEvery { generateQuestionUseCase.invoke(4) } returns Result.success(expectedQuestion)
            highScoreFlow.emit(10)

            viewModel
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.onQuestionAnswered(expectedQuestion, AIREDALE)

            val currentState = viewModel.uiState.value
            assertTrue(currentState.isWrongAnswer)
            assertFalse(currentState.isSuccess)
            assertEquals(0, currentState.currentScore)
            verify { highScoreRepository.saveHighScore(0) }
            assertFalse(currentState.isNewHighScore)
        }

    @Test
    fun `Given loading state When fetchQuestion is called Then state should show loading`() =
        runTest {
            val expectedQuestion = Question(
                options = listOf(AIREDALE, AKITA, KELPIE_AUSTRALIAN, SHEPHERD_AUSTRALIAN),
                correctOption = KELPIE_AUSTRALIAN,
                imageUrl = "imageUrl"
            )
            coEvery { generateQuestionUseCase.invoke(4) } coAnswers {
                delay(1000)
                Result.success(expectedQuestion)
            }
            viewModel

            assertTrue(viewModel.uiState.value.isLoading)

            // Then - advance and verify loading is false
            testDispatcher.scheduler.advanceTimeBy(1500)
            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(expectedQuestion, viewModel.uiState.value.question)
        }


    companion object {
        private val AIREDALE = DogBreed(name = "airedale", code = "airedale")
        private val AKITA = DogBreed(name = "akita", code = "akita")
        private val KELPIE_AUSTRALIAN =
            DogBreed(name = "kelpie australian", code = "australian/kelpie")
        private val SHEPHERD_AUSTRALIAN =
            DogBreed(name = "shepherd australian", code = "australian/shepherd")
        private val GREAT_DANE = DogBreed(name = "great dane", code = "dane/great")
    }

}