package com.ahmetsirim.data.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.ahmetsirim.common.log.log
import com.ahmetsirim.common.log.logError
import com.ahmetsirim.domain.model.SpeechResult
import com.ahmetsirim.domain.repository.AndroidSpeechRecognizerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AndroidSpeechRecognizerRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : AndroidSpeechRecognizerRepository {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false

    override fun startListening(
        languageCode: String,
    ): Flow<SpeechResult> = callbackFlow {

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            log(message = "Speech recognition not available", tag = TAG)
            trySend(SpeechResult.Error)
            close()
            return@callbackFlow
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(
                object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) = Unit /* no-op */

                    override fun onBeginningOfSpeech() {
                        log(message = "Beginning of speech", tag = TAG)
                        trySend(SpeechResult.BeginningOfSpeech)
                    }

                    override fun onRmsChanged(rmsdB: Float) = Unit /* no-op */

                    override fun onBufferReceived(buffer: ByteArray?) = Unit /* no-op */

                    override fun onEndOfSpeech() {
                        log(message = "End of speech", tag = TAG)
                        trySend(SpeechResult.EndOfSpeech)
                    }

                    override fun onError(error: Int) {
                        trySend(SpeechResult.Error)
                        isListening = false
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val confidence = results?.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)

                        if (!matches.isNullOrEmpty()) {
                            val bestMatch = matches[0]
                            val confidenceScore = confidence?.getOrNull(0) ?: 0.0f

                            log(
                                message = "Speech recognized: '$bestMatch' (confidence: $confidenceScore)",
                                tag = TAG
                            )

                            trySend(
                                SpeechResult.FinalResult(
                                    text = bestMatch,
                                    confidence = confidenceScore.toDouble(),
                                    allResults = matches
                                )
                            )
                        } else {
                            log(message = "No speech results", tag = TAG)
                            trySend(SpeechResult.Error)
                        }

                        isListening = false
                    }

                    override fun onPartialResults(partialResults: Bundle?) = Unit /* no-op */

                    override fun onEvent(eventType: Int, params: Bundle?) = Unit /* no-op */
                }
            )
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageCode)
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        }

        try {
            speechRecognizer?.startListening(intent)
            isListening = true
            log(message = "Started listening with language: $languageCode", tag = TAG)
        } catch (e: Exception) {
            logError(throwable = e, message = "Failed to start listening: ${e.message}", tag = TAG)
            trySend(SpeechResult.Error)
            close()
        }

        awaitClose {
            log(message = "Stopping speech recognition", tag = TAG)
            stopListening()
        }
    }

    override fun stopListening() {
        if (isListening) {
            speechRecognizer?.stopListening()
            isListening = false
            log(message = "Speech recognition stopped", tag = TAG)
        }
    }

    override fun cleanup() {
        log(message = "Cleaning up speech recognizer", tag = TAG)
        speechRecognizer?.destroy()
        speechRecognizer = null
        isListening = false
    }

    companion object {
        private const val TAG = "AndroidSpeechRecognizer"
    }
}