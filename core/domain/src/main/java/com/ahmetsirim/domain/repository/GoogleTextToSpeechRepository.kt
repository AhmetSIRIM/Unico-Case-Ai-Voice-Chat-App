package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.VoiceGenderEnum

interface GoogleTextToSpeechRepository {

    suspend fun speak(text: String, voiceGenderEnum: VoiceGenderEnum)

    fun stopSpeaking()

    fun cleanup()

}