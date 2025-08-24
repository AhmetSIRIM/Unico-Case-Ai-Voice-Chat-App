package com.ahmetsirim.domain.usecase.chat

import com.ahmetsirim.domain.repository.AndroidSpeechRecognizerRepository
import com.ahmetsirim.domain.repository.GoogleTextToSpeechRepository
import javax.inject.Inject

class CleanupResourcesUseCase @Inject constructor(
    private val googleTextToSpeechRepository: GoogleTextToSpeechRepository,
    private val androidSpeechRecognizerRepository: AndroidSpeechRecognizerRepository
) {
    operator fun invoke() {
        googleTextToSpeechRepository.cleanup()
        androidSpeechRecognizerRepository.cleanup()
    }
}
