package com.rickyputrah.pawquiz.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {
    @GET("breeds/list/all")
    suspend fun getBreedList(): DogBreedList

    @GET("breed/{breed}/images/random")
    suspend fun getDogImage(
        @Path("breed", encoded = true) breed: String
    ): DogImage
}

@JsonClass(generateAdapter = true)
data class DogBreedList(
    @Json(name = "message") val message: Map<String, List<String>>,
)

@JsonClass(generateAdapter = true)
data class DogImage(
    @field:Json(name = "message") val imageUrl: String
)