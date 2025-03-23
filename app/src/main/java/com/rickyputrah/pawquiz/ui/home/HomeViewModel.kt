package com.rickyputrah.pawquiz.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickyputrah.pawquiz.di.IoDispatcher
import com.rickyputrah.pawquiz.di.MainDispatcher
import com.rickyputrah.pawquiz.domain.HighScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val highScoreRepository: HighScoreRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _score = MutableStateFlow<Int>(0)
    val score = _score.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            highScoreRepository.getHighScore().collectLatest { highScore ->
                withContext(mainDispatcher) {
                    _score.value = highScore
                }
            }
        }
    }
}