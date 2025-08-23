package com.ahmetsirim.data.di.network

import com.ahmetsirim.data.api.GoogleTextToSpeechApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
internal object ApiModule {

    @Provides
    @ViewModelScoped
    internal fun provideGoogleTextToSpeechApi(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient,
    ): GoogleTextToSpeechApi {
        return Retrofit.Builder()
            .baseUrl(TEXT_TO_SPEECH_BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .build()
            .create(GoogleTextToSpeechApi::class.java)
    }

    private const val TEXT_TO_SPEECH_BASE_URL = "https://texttospeech.googleapis.com/"

}