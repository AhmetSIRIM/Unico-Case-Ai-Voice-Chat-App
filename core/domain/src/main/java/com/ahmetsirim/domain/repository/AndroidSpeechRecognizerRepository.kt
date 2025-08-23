package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.SpeechResult
import java.util.Locale
import kotlinx.coroutines.flow.Flow

interface AndroidSpeechRecognizerRepository {
    fun startListening(
        languageCode: String = Locale.getDefault().toString()
    ): Flow<SpeechResult>

    fun stopListening()
    fun cleanup()
}
