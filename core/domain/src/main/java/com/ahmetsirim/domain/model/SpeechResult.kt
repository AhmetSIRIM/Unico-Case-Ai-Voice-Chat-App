package com.ahmetsirim.domain.model

sealed class SpeechResult {
    object BeginningOfSpeech : SpeechResult()

    data class RmsChanged(val rmsdb: Float) : SpeechResult()
    object EndOfSpeech : SpeechResult()
    data class FinalResult(
        val text: String,
        val confidence: Double,
        val allResults: List<String>,
    ) : SpeechResult()

    data class Error(val errorMessageResId: Int) : SpeechResult()
}
