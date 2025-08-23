package com.ahmetsirim.domain.repository

interface GoogleTextToSpeechRepository {

    suspend fun speak(text: String)

    fun stopSpeaking()

    fun cleanup()
}
