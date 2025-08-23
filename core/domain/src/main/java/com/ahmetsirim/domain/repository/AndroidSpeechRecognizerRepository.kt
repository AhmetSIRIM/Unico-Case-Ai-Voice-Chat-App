package com.ahmetsirim.domain.repository

import com.ahmetsirim.domain.model.SpeechResult
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface AndroidSpeechRecognizerRepository {
    fun startListening(
        languageCode: String = Locale.getDefault().toString(),
        partialResults: Boolean = true,
    ): Flow<SpeechResult>

    fun stopListening()
    fun cleanup()

}