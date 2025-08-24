package com.ahmetsirim.domain.usecase.chat

import com.ahmetsirim.domain.repository.GoogleTextToSpeechRepository
import javax.inject.Inject

class SpeakTextUseCase @Inject constructor(
    private val googleTextToSpeechRepository: GoogleTextToSpeechRepository
) {
    suspend operator fun invoke(text: String) {
        googleTextToSpeechRepository.speak(text)
    }
}
