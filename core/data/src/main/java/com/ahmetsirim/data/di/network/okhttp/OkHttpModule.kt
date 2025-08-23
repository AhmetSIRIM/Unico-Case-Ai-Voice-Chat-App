package com.ahmetsirim.data.di.network.okhttp

import com.ahmetsirim.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object OkHttpModule {

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(GENERAL_OKHTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(GENERAL_OKHTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(GENERAL_OKHTTP_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(GENERAL_OKHTTP_TIMEOUT, TimeUnit.MILLISECONDS)
            .apply { if (BuildConfig.DEBUG) addInterceptor(httpLoggingInterceptor) }
            .build()
    }

    private const val GENERAL_OKHTTP_TIMEOUT = 10_000L

}