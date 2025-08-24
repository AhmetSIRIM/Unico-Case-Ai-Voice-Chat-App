package com.ahmetsirim.domain.usecase.chat

import com.ahmetsirim.domain.model.SpeechResult
import com.ahmetsirim.domain.repository.AndroidSpeechRecognizerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class StartListeningForSpeechUseCase @Inject constructor(
    private val androidSpeechRecognizerRepository: AndroidSpeechRecognizerRepository
) {
    operator fun invoke(): Flow<SpeechResult> {
        return androidSpeechRecognizerRepository.startListening()
    }
}
