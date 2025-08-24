package com.ahmetsirim.data.api

import com.ahmetsirim.data.dto.tts.TTSRequest
import com.ahmetsirim.data.dto.tts.TTSResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

fun interface GoogleTextToSpeechApi {
    @POST("v1/text:synthesize")
    suspend fun synthesizeSpeech(
        @Header("X-Goog-Api-Key")
        apiKey: String,
        @Body request: TTSRequest
    ): TTSResponse
}
