package com.ahmetsirim.data.dto.tts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TTSAudioConfig(
    @param:Json(name = "audioEncoding")
    val audioEncoding: String,
    @param:Json(name = "speakingRate")
    val speakingRate: Double = 1.0,
    @param:Json(name = "pitch")
    val pitch: Double = 0.0,
    @param:Json(name = "volumeGainDb")
    val volumeGainDb: Double = 16.0,
)