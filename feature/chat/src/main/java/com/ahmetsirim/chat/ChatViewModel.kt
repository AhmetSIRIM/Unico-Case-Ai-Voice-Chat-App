package com.ahmetsirim.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.SpeechResult
import com.ahmetsirim.domain.model.VoiceGenderEnum
import com.ahmetsirim.domain.model.common.Response
import com.ahmetsirim.domain.repository.AndroidSpeechRecognizerRepository
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
import com.ahmetsirim.domain.repository.GoogleTextToSpeechRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ChatViewModel @Inject constructor(
    private val generativeAiModelRepository: GenerativeAiModelRepository,
    private val androidSpeechRecognizerRepository: AndroidSpeechRecognizerRepository,
    private val googleTextToSpeechRepository: GoogleTextToSpeechRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatContract.UiState())
    val uiState: StateFlow<ChatContract.UiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = ChatContract.UiState()
        )

    fun onEvent(event: ChatContract.UiEvent) {
        when (event) {
            ChatContract.UiEvent.OnTheUserIsListened -> startListeningForSpeech()
            is ChatContract.UiEvent.UserSendTheMessage -> getMessageWithContext(message = event.message, chatHistory = event.chatHistory)
            ChatContract.UiEvent.UserNotifiedTheError -> _uiState.update { it.copy(errorState = null) }
            ChatContract.UiEvent.OnShowMicrophonePermissionRationale -> _uiState.update { it.copy(isRecordAudioPermissionRationaleInformationalDialogOpen = !it.isRecordAudioPermissionRationaleInformationalDialogOpen) }
        }
    }

    private fun startListeningForSpeech() {
        viewModelScope.launch {
            androidSpeechRecognizerRepository.startListening().collect { speechResult ->
                if (speechResult is SpeechResult.FinalResult) {
                    _uiState.update { state -> state.copy(speechResult = speechResult) }
                    getMessageWithContext(message = speechResult.text, chatHistory = _uiState.value.messages)
                }
            }
        }
    }

    private fun getMessageWithContext(message: String, chatHistory: List<ChatMessage>) {
        viewModelScope.launch {
            generativeAiModelRepository.generateContentWithContext(message = message, chatHistory = chatHistory).collect { response ->
                when (response) {
                    is Response.Error -> _uiState.update {
                        it.copy(
                            isAiSpeaking = false,
                            errorState = response.errorState
                        )
                    }

                    is Response.Loading -> _uiState.update {
                        it.copy(
                            errorState = null,
                            isAiSpeaking = true,
                            messages = it.messages + ChatMessage(isFromUser = true, content = message)
                        )
                    }

                    is Response.Success -> {
                        googleTextToSpeechRepository.speak(
                            text = response.result,
                            voiceGenderEnum = VoiceGenderEnum.FEMALE
                        )

                        _uiState.update {
                            it.copy(
                                isAiSpeaking = false,
                                errorState = null,
                                messages = it.messages + ChatMessage(isFromUser = false, content = response.result)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        googleTextToSpeechRepository.cleanup()
        androidSpeechRecognizerRepository.cleanup()

        super.onCleared()
    }

}