package com.ahmetsirim.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.SpeechResult
import com.ahmetsirim.domain.model.common.Response
import com.ahmetsirim.domain.repository.AndroidSpeechRecognizerRepository
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
import com.ahmetsirim.domain.repository.GoogleTextToSpeechRepository
import com.ahmetsirim.domain.repository.LocalChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ChatViewModel @Inject constructor(
    private val generativeAiModelRepository: GenerativeAiModelRepository,
    private val androidSpeechRecognizerRepository: AndroidSpeechRecognizerRepository,
    private val googleTextToSpeechRepository: GoogleTextToSpeechRepository,
    private val localChatRepository: LocalChatRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val sessionId = savedStateHandle.toRoute<ChatRoute>().sessionId

    private val _uiState = MutableStateFlow(ChatContract.UiState())
    val uiState: StateFlow<ChatContract.UiState> = _uiState
        .onStart { getMessagesBySessionId(sessionId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = ChatContract.UiState()
        )

    fun onEvent(event: ChatContract.UiEvent) {
        when (event) {
            ChatContract.UiEvent.OnTheUserIsListened -> startListeningForSpeech()
            ChatContract.UiEvent.UserNotifiedTheError -> _uiState.update { it.copy(errorState = null) }
            ChatContract.UiEvent.OnShowMicrophonePermissionRationale -> _uiState.update { it.copy(isRecordAudioPermissionRationaleInformationalDialogOpen = !it.isRecordAudioPermissionRationaleInformationalDialogOpen) }
        }
    }

    private suspend fun getMessagesBySessionId(sessionId: String?) {
        sessionId ?: return

        val messages: List<ChatMessage> = localChatRepository
            .getChatById(sessionId)
            .getOrNull()
            ?.messages
            ?: emptyList()

        _uiState.update { it.copy(messages = messages) }
    }

    private fun startListeningForSpeech() {
        viewModelScope.launch {
            androidSpeechRecognizerRepository.startListening().collect { speechResult ->
                if (speechResult is SpeechResult.FinalResult) {
                    if (_uiState.value.messages.isEmpty()) createNewChatSession()

                    localChatRepository.saveMessage(_uiState.value.currentSessionId, ChatMessage(isFromUser = true, content = speechResult.text))

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
                            speechResult = null,
                            messages = it.messages + ChatMessage(isFromUser = true, content = message)
                        )
                    }

                    is Response.Success -> {
                        coroutineScope {
                            localChatRepository.saveMessage(
                                sessionId = _uiState.value.currentSessionId,
                                message = ChatMessage(
                                    isFromUser = false,
                                    content = response.result
                                )
                            )
                        }

                        coroutineScope { googleTextToSpeechRepository.speak(text = response.result) }

                        _uiState.update {
                            it.copy(
                                isAiSpeaking = false,
                                errorState = null,
                                speechResult = null,
                                messages = it.messages + ChatMessage(isFromUser = false, content = response.result)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createNewChatSession() {
        viewModelScope.launch {
            val sessionId = localChatRepository.createNewChatSession()

            _uiState.update {
                it.copy(
                    currentSessionId = sessionId,
                    messages = emptyList()
                )
            }
        }
    }

    override fun onCleared() {
        googleTextToSpeechRepository.cleanup()
        androidSpeechRecognizerRepository.cleanup()

        super.onCleared()
    }

}