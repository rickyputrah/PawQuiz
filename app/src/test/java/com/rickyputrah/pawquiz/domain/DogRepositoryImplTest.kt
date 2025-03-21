package com.rickyputrah.pawquiz.domain

import com.rickyputrah.pawquiz.data.DogApi
import com.rickyputrah.pawquiz.data.DogBreedList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DogRepositoryImplTest {
    private val dogApi: DogApi = mockk(relaxed = true)

    private val repository by lazy(LazyThreadSafetyMode.NONE) {
        DogRepositoryImpl(
            ioDispatcher = UnconfinedTestDispatcher(),
            dogApi = dogApi
        )
    }

    @Test
    fun `Given api return result When getDogBreedList then return list of dog`() = runTest {
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
    }

    @Test
    fun `Given api return error When getDogBreedList then return result failure`() = runTest {
        coEvery { dogApi.getBreedList() } throws Throwable()

        val result = repository.getDogBreedList()

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