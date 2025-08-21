package com.ahmetsirim.common.di.coroutine.dispatcher

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatcherModule {

    @Provides
    @Singleton
    @DispatcherQualifier(DispatcherTypeEnum.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @DispatcherQualifier(DispatcherTypeEnum.DEFAULT)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @DispatcherQualifier(DispatcherTypeEnum.MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}