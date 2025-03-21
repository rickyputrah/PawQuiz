package com.rickyputrah.pawquiz.di

import com.rickyputrah.pawquiz.domain.DogRepository
import com.rickyputrah.pawquiz.domain.DogRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DogRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDogRepository(impl: DogRepositoryImpl): DogRepository
}