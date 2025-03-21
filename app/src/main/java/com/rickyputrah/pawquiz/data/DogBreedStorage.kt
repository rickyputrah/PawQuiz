package com.rickyputrah.pawquiz.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.rickyputrah.pawquiz.di.DogBreedStorageModule.Companion.DOG_BREED_PREFS
import com.rickyputrah.pawquiz.domain.DogBreed
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import javax.inject.Named

interface DogBreedStorage {
    fun getBreedList(): Result<List<DogBreed>?>
    fun storeBreedList(dogBreeds: List<DogBreed>)
}

internal class DogBreedStorageImpl @Inject constructor(
    private val moshi: Moshi,
    @Named(DOG_BREED_PREFS) private val preferences: SharedPreferences,
) : DogBreedStorage {
    override fun getBreedList(): Result<List<DogBreed>?> {
        return runCatching {
            val json = preferences.getString(DOG_BREED_LIST_KEY, null)

            if (json != null) {
                val type = Types.newParameterizedType(List::class.java, DogBreed::class.java)
                val adapter = moshi.adapter<List<DogBreed>>(type)
                adapter.fromJson(json)
            } else {
                null
            }
        }
    }

    override fun storeBreedList(dogBreeds: List<DogBreed>) {
        runCatching {
            preferences.edit {
                val type = Types.newParameterizedType(List::class.java, DogBreed::class.java)
                val adapter = moshi.adapter<List<DogBreed>>(type)
                putString(DOG_BREED_LIST_KEY, adapter.toJson(dogBreeds))
            }
        }
    }

    companion object {
        private const val DOG_BREED_LIST_KEY = "dog_breed_list_key"
    }
}