package com.ahmetsirim.data.api

import com.ahmetsirim.data.dto.tts.TTSRequest
import com.ahmetsirim.data.dto.tts.TTSResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GoogleTextToSpeechApi {

    // TODO: The query here is problematic in terms of security. This will be converted to a header.
    @POST("v1/text:synthesize")
    suspend fun synthesizeSpeech(
        @Query("key")
        apiKey: String,
        @Body request:
        TTSRequest,
    ): TTSResponse

}

