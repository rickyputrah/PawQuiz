package com.rickyputrah.pawquiz.domain

import com.rickyputrah.pawquiz.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GenerateQuestionUseCase {
    suspend operator fun invoke(numOfOption: Int): Result<Question>
}

class GenerateQuestionUseCaseImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dogRepository: DogRepository,
) : GenerateQuestionUseCase {
    override suspend fun invoke(numOfOption: Int): Result<Question> {
        return withContext(ioDispatcher) {
            val dogBreedList =
                dogRepository.getDogBreedList().getOrNull()
                    ?: return@withContext Result.failure(QuestionException.FailedToFetchDogBreedList())

            val shuffledDogList = dogBreedList.shuffled().take(numOfOption)
            val correctOption = shuffledDogList.first()

            val imageUrl = dogRepository.getDogImage(code = correctOption.code).getOrNull()
                ?: return@withContext Result.failure(QuestionException.FailedToFetchImage())

            Result.success(
                Question(
                    options = shuffledDogList.shuffled(),
                    correctOption = correctOption,
                    imageUrl = imageUrl
                )
            )
        }
    }
}

sealed class QuestionException : Throwable() {
    class FailedToFetchDogBreedList : QuestionException()
    class FailedToFetchImage : QuestionException()
}

data class Question(
    val options: List<DogBreed>,
    val correctOption: DogBreed,
    val imageUrl: String?
)