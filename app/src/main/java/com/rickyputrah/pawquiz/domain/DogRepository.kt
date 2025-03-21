package com.rickyputrah.pawquiz.domain

import androidx.annotation.Keep
import com.rickyputrah.pawquiz.data.DogApi
import com.rickyputrah.pawquiz.di.IoDispatcher
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DogRepository {
    suspend fun getDogBreedList(): Result<List<DogBreed>>
}

class DogRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dogApi: DogApi,
) : DogRepository {

    override suspend fun getDogBreedList(): Result<List<DogBreed>> {
        return withContext(ioDispatcher) {
            runCatching {
                fetchBreedList()
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