package com.ahmetsirim.data.dto.tts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TTSVoice(
    @param:Json(name = "languageCode")
    val languageCode: String,
    @param:Json(name = "name")
    val name: String
)
