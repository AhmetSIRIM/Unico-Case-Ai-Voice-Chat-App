package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.SpeechResult
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface AndroidSpeechRecognizerRepository {
    fun startListening(
        languageCode: String = Locale.getDefault().toString(),
    ): Flow<SpeechResult>

    fun stopListening()
    fun cleanup()

}