package com.ahmetsirim.data.di

import com.ahmetsirim.data.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance().apply {
            val isThisTheDebugBuildVariant = BuildConfig.DEBUG
            isCrashlyticsCollectionEnabled = !isThisTheDebugBuildVariant
        }
    }

}