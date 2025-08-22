package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.VoiceGenderEnum

fun interface GoogleTextToSpeechRepository {

    suspend fun speak(text: String,voiceGenderEnum: VoiceGenderEnum)

}