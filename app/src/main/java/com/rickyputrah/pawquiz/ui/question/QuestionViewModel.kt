package com.rickyputrah.pawquiz.ui.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickyputrah.pawquiz.di.IoDispatcher
import com.rickyputrah.pawquiz.di.MainDispatcher
import com.rickyputrah.pawquiz.domain.DogBreed
import com.rickyputrah.pawquiz.domain.GenerateQuestionUseCase
import com.rickyputrah.pawquiz.domain.HighScoreRepository
import com.rickyputrah.pawquiz.domain.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val generateQuestionUseCase: GenerateQuestionUseCase,
    private val highScoreRepository: HighScoreRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuestionUiState>(QuestionUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        fetchQuestion()
    }

    fun fetchQuestion() = viewModelScope.launch(ioDispatcher) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        generateQuestionUseCase.invoke(NUMBER_OF_OPTION)
            .onSuccess { question ->
                withContext(mainDispatcher) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            question = question,
                            promptNumber = Random.nextInt(1, NUMBER_OF_PROMPT)
                        )
                    }
                }
            }.onFailure {
                withContext(mainDispatcher) {
                    _uiState.update {
                        it.copy(isLoading = false, question = null)
                    }
                }
            }
    }


    fun onQuestionAnswered(question: Question, option: DogBreed) {
        if (question.correctOption == option) {
            var newScore = _uiState.value.currentScore + 1
            _uiState.update {
                it.copy(isSuccess = true, currentScore = newScore)
            }
            getNextQuestion()
        } else {
            val currentScore = _uiState.value.currentScore
            val isNewHighScore = highScoreRepository.getHighScore().value < currentScore
            highScoreRepository.saveHighScore(currentScore)
            _uiState.update {
                it.copy(isWrongAnswer = true, isNewHighScore = isNewHighScore)
            }
        }
    }

    private fun getNextQuestion() {
        viewModelScope.launch(ioDispatcher) {
            val nextQuestion =
                viewModelScope.async {
                    generateQuestionUseCase.invoke(NUMBER_OF_OPTION).getOrNull()
                }
            viewModelScope.async(ioDispatcher) { delay(SUCCESS_DELAY) }.await()
            withContext(mainDispatcher) {
                _uiState.update {
                    it.copy(
                        promptNumber = Random.nextInt(1, NUMBER_OF_PROMPT),
                        isSuccess = false,
                        question = nextQuestion.await()
                    )
                }
            }
        }
    }

    data class QuestionUiState(
        val currentScore: Int = 0,
        val promptNumber: Int = 0,
        val isLoading: Boolean = true,
        val question: Question? = null,
        val isSuccess: Boolean = false,
        val isWrongAnswer: Boolean = false,
        val isNewHighScore: Boolean = false,
    )

    companion object {
        private const val SUCCESS_DELAY = 2_000L
        private const val NUMBER_OF_PROMPT = 13
        private const val NUMBER_OF_OPTION = 4
    }
}