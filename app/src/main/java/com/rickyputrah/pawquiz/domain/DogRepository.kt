package com.rickyputrah.pawquiz.domain

import androidx.annotation.Keep
import com.rickyputrah.pawquiz.data.DogApi
import com.rickyputrah.pawquiz.data.DogBreedStorage
import com.rickyputrah.pawquiz.di.IoDispatcher
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DogRepository {
    suspend fun getDogBreedList(): Result<List<DogBreed>>
    suspend fun getDogImage(code: String): Result<String>
}

class DogRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dogBreedStorage: DogBreedStorage,
    private val dogApi: DogApi,
) : DogRepository {

    override suspend fun getDogBreedList(): Result<List<DogBreed>> {
        return withContext(ioDispatcher) {
            runCatching {
                // Option: Improve this by fetching the breed list periodically (every few days or weeks)
                // if we expect the data to change frequently. Adding a timestamp to track the last fetch
                // and decide when to refresh the list.
                dogBreedStorage.getBreedList().getOrNull() ?: fetchBreedList()
            }
        }
    }

    override suspend fun getDogImage(code: String): Result<String> {
        return withContext(ioDispatcher) {
            runCatching {
                dogApi.getDogImage(breed = code).imageUrl
            }
        }
    }

    private suspend fun fetchBreedList(): List<DogBreed> {
        val dogBreeds = dogApi.getBreedList()
            .message
            .entries
            .fold(mutableListOf<DogBreed>()) { acc, (key, values) ->
                if (values.isEmpty()) {
                    acc.add(DogBreed.create(key))
                } else {
                    values.forEach { acc.add(DogBreed.create(key, it)) }
                }
                acc
            }
        dogBreedStorage.storeBreedList(dogBreeds)
        return dogBreeds
    }
}

@JsonClass(generateAdapter = true)
@Keep
data class DogBreed(val name: String, val code: String) {
    companion object {
        fun create(species: String, breed: String? = null): DogBreed {
            return DogBreed(
                name = if (breed.isNullOrEmpty()) species else "$breed $species",
                code = if (breed.isNullOrEmpty()) species else "$species/$breed"
            )
        }
    }
}