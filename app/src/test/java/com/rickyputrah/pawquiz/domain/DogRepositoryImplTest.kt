package com.rickyputrah.pawquiz.domain

import com.rickyputrah.pawquiz.data.DogApi
import com.rickyputrah.pawquiz.data.DogBreedList
import com.rickyputrah.pawquiz.data.DogBreedStorage
import com.rickyputrah.pawquiz.data.DogImage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DogRepositoryImplTest {
    private val dogApi: DogApi = mockk(relaxed = true)
    private val dogBreedStorage: DogBreedStorage = mockk(relaxed = true)

    private val repository by lazy(LazyThreadSafetyMode.NONE) {
        DogRepositoryImpl(
            ioDispatcher = UnconfinedTestDispatcher(),
            dogBreedStorage = dogBreedStorage,
            dogApi = dogApi
        )
    }

    @Test
    fun `Given storage return null and api return result When getDogBreedList then return list of dog from api`() =
        runTest {
            coEvery { dogBreedStorage.getBreedList() } returns Result.success(null)
            coEvery { dogApi.getBreedList() } returns DOG_API_RESULT

            val result = repository.getDogBreedList()

            val expectedList = listOf(
                DogBreed(name = "airedale", code = "airedale"),
                DogBreed(name = "akita", code = "akita"),
                DogBreed(name = "kelpie australian", code = "australian/kelpie"),
                DogBreed(name = "shepherd australian", code = "australian/shepherd"),
                DogBreed(name = "great dane", code = "dane/great")
            )

            assertEquals(result.getOrNull(), expectedList)
            coVerify { dogApi.getBreedList() }
        }

    @Test
    fun `Given storage return list of dog When getDogBreedList then return list of dog from local storage`() =
        runTest {
            val expectedList = listOf(
                DogBreed(name = "airedale", code = "airedale"),
                DogBreed(name = "akita", code = "akita"),
                DogBreed(name = "kelpie australian", code = "australian/kelpie"),
                DogBreed(name = "shepherd australian", code = "australian/shepherd"),
                DogBreed(name = "great dane", code = "dane/great")
            )
            coEvery { dogBreedStorage.getBreedList() } returns Result.success(expectedList)
            coEvery { dogApi.getBreedList() } returns DOG_API_RESULT

            val result = repository.getDogBreedList()

            assertEquals(result.getOrNull(), expectedList)
            coVerify(atLeast = 0) { dogApi.getBreedList() }
        }

    @Test
    fun `Given storage return null and api return error When getDogBreedList then return result failure`() =
        runTest {
            coEvery { dogBreedStorage.getBreedList() } returns Result.success(null)
            coEvery { dogApi.getBreedList() } throws Throwable()

            val result = repository.getDogBreedList()

            assertTrue(result.isFailure)
        }

    @Test
    fun `Given get dog image success When get dog image Then return expected image url `() =
        runTest {
            val code = "australian/kelpie"
            val expectedImageUrl = "imageUrl"
            coEvery { dogApi.getDogImage(code) } returns DogImage(expectedImageUrl)

            val result = repository.getDogImage(code = code)

            assertTrue(result.isSuccess)
            assertEquals(expectedImageUrl, result.getOrNull())
        }

    @Test
    fun `Given get dog image failed When get dog image Then return failure`() = runTest {
        val code = "australian/kelpie"
        coEvery { dogApi.getDogImage(code) } throws Throwable("failed to get dog image")

        val result = repository.getDogImage(code = code)

        assertTrue(result.isFailure)
    }

    companion object {
        private val DOG_API_RESULT = DogBreedList(
            message = mapOf(
                "airedale" to listOf(),
                "akita" to listOf(),
                "australian" to listOf("kelpie", "shepherd"),
                "dane" to listOf("great"),
            )
        )
    }
}