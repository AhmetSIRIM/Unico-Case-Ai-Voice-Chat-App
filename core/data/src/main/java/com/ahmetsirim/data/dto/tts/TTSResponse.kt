package com.ahmetsirim.data.dto.tts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TTSResponse(
    @param:Json(name = "audioContent")
    val audioContent: String
)
