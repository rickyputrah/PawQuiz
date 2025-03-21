package com.rickyputrah.pawquiz.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.rickyputrah.pawquiz.data.DogBreedStorage
import com.rickyputrah.pawquiz.data.DogBreedStorageImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
internal abstract class DogBreedStorageModule {

    @Binds
    @Singleton
    internal abstract fun bindDogBreedStorage(impl: DogBreedStorageImpl): DogBreedStorage

    companion object {
        @Provides
        @Singleton
        @Named(DOG_BREED_PREFS)
        fun provideDogBreedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(KEY, MODE_PRIVATE)
        }

        private const val KEY = "dog_breed_key"
        const val DOG_BREED_PREFS = "dog_breed_prefs"
    }
}