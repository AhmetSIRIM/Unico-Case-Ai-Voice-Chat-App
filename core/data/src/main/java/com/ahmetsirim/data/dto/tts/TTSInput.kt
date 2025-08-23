package com.ahmetsirim.data.dto.tts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TTSInput(
    @param:Json(name = "text")
    val text: String
)
