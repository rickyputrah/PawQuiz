package com.rickyputrah.pawquiz.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET

interface DogApi {
    @GET("breeds/list/all")
    suspend fun getBreedList(): DogBreedList
}

@JsonClass(generateAdapter = true)
data class DogBreedList(
    @Json(name = "message") val message: Map<String, List<String>>,
)