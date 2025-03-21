package com.rickyputrah.pawquiz.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerateQuestionUseCaseTest {
    private val dogRepository = mockk<DogRepository>()

    private val usecase by lazy(LazyThreadSafetyMode.NONE) {
        GenerateQuestionUseCaseImpl(
            ioDispatcher = UnconfinedTestDispatcher(),
            dogRepository = dogRepository,
        )
    }


    @Test
    fun `Given repository return list and image When invoke Then return question with the same size requested and image url `() =
        runTest {
            val expectedImageUrl = "image url"
            val numOfOption = 4
            coEvery { dogRepository.getDogBreedList() } returns Result.success(DOG_LIST)
            coEvery { dogRepository.getDogImage(any()) } returns Result.success("image url")

            val result = usecase.invoke(numOfOption = numOfOption)

            val question = result.getOrNull()
            // Verify the order of question is shuffled
            assertNotEquals(DOG_LIST.take(numOfOption), question!!.options)
            assertEquals(numOfOption, question.options.size)
            coVerify { dogRepository.getDogImage(question.correctOption.code) }
            assertEquals(expectedImageUrl, question.imageUrl)
        }

    @Test
    fun `Given repository to fetch dog list throw an error When invoke Then return exception`() =
        runTest {
            val numOfOption = 4
            coEvery { dogRepository.getDogBreedList() } returns Result.failure(Throwable("failed to fetch dog breed list"))

            val result = usecase.invoke(numOfOption = numOfOption)

            assertTrue(result.exceptionOrNull() is QuestionException.FailedToFetchDogBreedList)
            coVerify(atLeast = 0) { dogRepository.getDogImage(any()) }
        }

    @Test
    fun `Given repository to fetch dog image throw an error When invoke Then return exception`() =
        runTest {
            val numOfOption = 4
            coEvery { dogRepository.getDogBreedList() } returns Result.success(DOG_LIST)
            coEvery { dogRepository.getDogImage(any()) } returns Result.failure(Throwable("failed to fetch image"))

            val result = usecase.invoke(numOfOption = numOfOption)

            assertTrue(result.exceptionOrNull() is QuestionException.FailedToFetchImage)
            coVerify(atLeast = 0) { dogRepository.getDogImage(any()) }
        }


    companion object {
        val DOG_LIST = listOf(
            DogBreed(name = "airedale", code = "airedale"),
            DogBreed(name = "akita", code = "akita"),
            DogBreed(name = "kelpie australian", code = "australian/kelpie"),
            DogBreed(name = "shepherd australian", code = "australian/shepherd"),
            DogBreed(name = "great dane", code = "dane/great")
        )
    }
}