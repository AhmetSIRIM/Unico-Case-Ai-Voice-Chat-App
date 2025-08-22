package com.ahmetsirim.data.di

import com.ahmetsirim.data.repository.GenerativeAiModelRepositoryImpl
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
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
    abstract fun bindRemoteGenerativeAiModelDataSource(
        generativeAiModelRepositoryImpl: GenerativeAiModelRepositoryImpl,
    ): GenerativeAiModelRepository

}