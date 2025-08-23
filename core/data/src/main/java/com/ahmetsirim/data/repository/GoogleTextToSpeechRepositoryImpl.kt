package com.ahmetsirim.data.repository

import android.media.MediaPlayer
import android.util.Base64
import com.ahmetsirim.common.log.log
import com.ahmetsirim.common.log.logError
import com.ahmetsirim.data.BuildConfig
import com.ahmetsirim.data.api.GoogleTextToSpeechApi
import com.ahmetsirim.data.di.FileOperationsHelper
import com.ahmetsirim.data.dto.tts.TTSAudioConfig
import com.ahmetsirim.data.dto.tts.TTSInput
import com.ahmetsirim.data.dto.tts.TTSRequest
import com.ahmetsirim.data.dto.tts.TTSVoice
import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.repository.AppSettingsRepository
import com.ahmetsirim.domain.repository.GoogleTextToSpeechRepository
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GoogleTextToSpeechRepositoryImpl @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val googleTextToSpeechApi: GoogleTextToSpeechApi,
    private val fileOperationsHelper: FileOperationsHelper,
    private val mediaPlayer: MediaPlayer
) : GoogleTextToSpeechRepository {

    private suspend fun synthesizeSpeech(
        text: String,
        voiceGenderEnum: VoiceGenderEnum,
        language: String = Locale.getDefault().toLanguageTag()
    ): ByteArray = withContext(Dispatchers.IO) {
        try {
            val response = googleTextToSpeechApi.synthesizeSpeech(
                apiKey = BuildConfig.GOOGLE_CLOUD_TEXT_TO_SPEECH_API_KEY,
                request = TTSRequest(
                    input = TTSInput(text = text),
                    voice = TTSVoice(
                        languageCode = language,
                        name = "$language${voiceGenderEnum.voiceName}"
                    ),
                    audioConfig = TTSAudioConfig(
                        audioEncoding = AUDIO_ENCODING,
                        speakingRate = 1.0,
                        pitch = 0.0,
                        volumeGainDb = 12.0
                    )
                )
            )

            val audioBytes = Base64.decode(response.audioContent, Base64.DEFAULT)
            log(message = "Audio decoded successfully, size: ${audioBytes.size} bytes", tag = TAG)

            audioBytes
        } catch (e: HttpException) {
            logError(
                throwable = e,
                message = "TTS API HTTP Error: ${e.code()} - ${e.message()}",
                tag = TAG
            )
            logError(
                throwable = e,
                message = "Error body: ${e.response()?.errorBody()?.string()}",
                tag = TAG
            )

            throw IOException("TTS API HTTP Error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            logError(throwable = e, message = "TTS API Error: ${e.message}", tag = TAG)

            throw e
        }
    }

    override fun stopSpeaking() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.reset()
            }
        } catch (e: Exception) {
            logError(throwable = e, message = "Error stopping MediaPlayer: ${e.message}", tag = TAG)
        }
    }

    private fun playAudio(
        audioBytes: ByteArray,
        callback: (success: Boolean, error: String?) -> Unit
    ) {
        log("playAudio() called with ${audioBytes.size} bytes", tag = TAG)

        try {
            val tempFile = fileOperationsHelper.createTempAudioFile(audioBytes)
            log("Temp file created: ${tempFile.absolutePath}", tag = TAG)

            setupMediaPlayer(tempFile.absolutePath)
        } catch (e: Exception) {
            logError(throwable = e, message = "Play audio error: ${e.message}", tag = TAG)
            callback(false, e.message)
        }
    }

    private fun setupMediaPlayer(
        filePath: String
    ) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(filePath)

            mediaPlayer.setOnPreparedListener {
                log("MediaPlayer prepared, starting playback...", tag = TAG)
                mediaPlayer.start()
            }

            mediaPlayer.setOnCompletionListener {
                log("MediaPlayer playback completed", tag = TAG)
                fileOperationsHelper.cleanupTempFile(filePath)
            }

            mediaPlayer.setOnErrorListener { _, what, extra ->
                logError(
                    throwable = Exception("MediaPlayer error: what=$what, extra=$extra"),
                    message = "MediaPlayer error",
                    tag = TAG
                )
                fileOperationsHelper.cleanupTempFile(filePath)
                true
            }

            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            logError(throwable = e, message = "Setup MediaPlayer error: ${e.message}", tag = TAG)
        }
    }

    override suspend fun speak(text: String) {
        log("speak() called with text: '${text.take(50)}...'")

        val voiceGenderEnum = appSettingsRepository
            .getAppSettings()
            .firstOrNull()
            ?.getOrNull()
            ?.voiceGender
            ?: VoiceGenderEnum.FEMALE

        if (text.isBlank()) {
            log(message = "Text is empty!", tag = TAG)
            return
        }

        try {
            log("Starting TTS synthesis...", tag = TAG)

            stopSpeaking()

            val audioBytes = synthesizeSpeech(text, voiceGenderEnum)
            log("Audio synthesized successfully, size: ${audioBytes.size} bytes", tag = TAG)

            withContext(Dispatchers.Main) {
                playAudio(audioBytes) { success, error ->
                    log("Audio play result - success: $success, error: $error", tag = TAG)
                }
            }
        } catch (e: Exception) {
            logError(throwable = e, message = "Speak error: ${e.message}", tag = TAG)
            withContext(Dispatchers.Main) {
            }
        }
    }

    override fun cleanup() {
        log("cleanup() called", tag = TAG)
        stopSpeaking()
    }

    companion object {
        private const val TAG = "TextToSpeechRepo"
        private const val AUDIO_ENCODING = "MP3"
    }
}
