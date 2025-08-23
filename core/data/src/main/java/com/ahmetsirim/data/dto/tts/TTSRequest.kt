package com.ahmetsirim.data.dto.tts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TTSRequest(
    @param:Json(name = "input")
    val input: TTSInput,
    @param:Json(name = "voice")
    val voice: TTSVoice,
    @param:Json(name = "audioConfig")
    val audioConfig: TTSAudioConfig
)
