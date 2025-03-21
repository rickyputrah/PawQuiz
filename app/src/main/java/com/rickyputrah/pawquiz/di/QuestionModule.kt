package com.rickyputrah.pawquiz.di

import com.rickyputrah.pawquiz.domain.GenerateQuestionUseCase
import com.rickyputrah.pawquiz.domain.GenerateQuestionUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class QuestionModule {
    @Binds
    @Singleton
    abstract fun bindDGenerateQuestionUseCase(impl: GenerateQuestionUseCaseImpl): GenerateQuestionUseCase
}