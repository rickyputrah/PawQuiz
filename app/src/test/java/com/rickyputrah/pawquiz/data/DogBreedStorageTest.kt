package com.rickyputrah.pawquiz.data

import android.content.SharedPreferences
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import com.rickyputrah.pawquiz.domain.DogBreed
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DogBreedStorageTest {
    private val pref = SPMockBuilder().createSharedPreferences()

    private val storage by lazy(LazyThreadSafetyMode.NONE) {
        DogBreedStorageImpl(
            moshi = Moshi.Builder().build(),
            preferences = pref
        )
    }

    @Test
    fun `Given breed list stored When get breed list Then return saved breed list`() {
        storage.storeBreedList(DOG_BREED_OLD_LIST)
        assertEquals(DOG_BREED_OLD_LIST, storage.getBreedList().getOrNull())

        storage.storeBreedList(DOG_BREED_NEW_LIST)
        assertEquals(DOG_BREED_NEW_LIST, storage.getBreedList().getOrNull())
    }

    @Test
    fun `Given breed list is not stored When get breed list Then return empty`() {
        val result = storage.getBreedList()
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun `Given error occurs when storing breed list When get breed list Then return empty`() {
        val moshi: Moshi = mockk(relaxed = true)
        val preferences: SharedPreferences = mockk(relaxed = true)

        every { preferences.edit() } throws RuntimeException("Failed to save")

        val storage = DogBreedStorageImpl(moshi = moshi, preferences = preferences)
        storage.storeBreedList(DOG_BREED_OLD_LIST)

        assertNull(storage.getBreedList().getOrNull())
    }

    @Test
    fun `Given failure in serializing breed list When get breed list Then return empty`() {
        val moshi: Moshi = mockk(relaxed = true)
        val adapter = mockk<com.squareup.moshi.JsonAdapter<List<DogBreed>>>()
        every { moshi.adapter<List<DogBreed>>(List::class.java) } returns adapter
        every { adapter.toJson(any()) } throws RuntimeException("Serialization failed")

        val storage = DogBreedStorageImpl(moshi = moshi, preferences = pref)
        storage.storeBreedList(DOG_BREED_OLD_LIST)

        assertNull(storage.getBreedList().getOrNull())
    }

    companion object {
        private val DOG_BREED_OLD_LIST = listOf(
            DogBreed(name = "kelpie australian", code = "australian/kelpie"),
            DogBreed(name = "shepherd australian", code = "australian/shepherd"),
            DogBreed(name = "great dane", code = "dane/great")
        )

        private val DOG_BREED_NEW_LIST = listOf(
            DogBreed(name = "airedale", code = "airedale"),
            DogBreed(name = "akita", code = "akita"),
            DogBreed(name = "kelpie australian", code = "australian/kelpie"),
            DogBreed(name = "shepherd australian", code = "australian/shepherd"),
            DogBreed(name = "great dane", code = "dane/great")
        )
    }
}
