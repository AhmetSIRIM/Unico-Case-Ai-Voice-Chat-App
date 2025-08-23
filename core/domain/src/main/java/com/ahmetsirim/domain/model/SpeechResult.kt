package com.ahmetsirim.domain.model

sealed class SpeechResult {
    object BeginningOfSpeech : SpeechResult()
    object EndOfSpeech : SpeechResult()
    data class FinalResult(
        val text: String,
        val confidence: Double,
        val allResults: List<String>
    ) : SpeechResult()
    data object Error : SpeechResult()
}
