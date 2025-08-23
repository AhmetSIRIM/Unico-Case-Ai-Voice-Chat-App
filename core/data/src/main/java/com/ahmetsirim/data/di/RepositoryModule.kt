package com.ahmetsirim.data.di

import com.ahmetsirim.data.repository.AndroidSpeechRecognizerRepositoryImpl
import com.ahmetsirim.data.repository.GenerativeAiModelRepositoryImpl
import com.ahmetsirim.data.repository.GoogleTextToSpeechRepositoryImpl
import com.ahmetsirim.domain.repository.AndroidSpeechRecognizerRepository
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
import com.ahmetsirim.domain.repository.GoogleTextToSpeechRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGenerativeAiModelRepository(
        generativeAiModelRepositoryImpl: GenerativeAiModelRepositoryImpl,
    ): GenerativeAiModelRepository

    @Binds
    @ViewModelScoped
    abstract fun bindTextToSpeechRepository(
        googleTextToSpeechRepositoryImpl: GoogleTextToSpeechRepositoryImpl,
    ): GoogleTextToSpeechRepository

    @Binds
    @ViewModelScoped
    abstract fun bindAndroidSpeechRecognizerRepository(
        androidSpeechRecognizerRepositoryImpl: AndroidSpeechRecognizerRepositoryImpl,
    ): AndroidSpeechRecognizerRepository

}