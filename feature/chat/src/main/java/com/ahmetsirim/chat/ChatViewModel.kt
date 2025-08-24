package com.ahmetsirim.chat

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ahmetsirim.common.networkmonitor.NetworkMonitor
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.SpeechResult
import com.ahmetsirim.domain.model.common.Response
import com.ahmetsirim.domain.usecase.chat.CleanupResourcesUseCase
import com.ahmetsirim.domain.usecase.chat.CreateNewChatSessionUseCase
import com.ahmetsirim.domain.usecase.chat.GenerateContentWithContextUseCase
import com.ahmetsirim.domain.usecase.chat.GetMessagesBySessionIdUseCase
import com.ahmetsirim.domain.usecase.chat.SaveMessageUseCase
import com.ahmetsirim.domain.usecase.chat.SpeakTextUseCase
import com.ahmetsirim.domain.usecase.chat.StartListeningForSpeechUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class ChatViewModel @Inject constructor(
    private val getMessagesBySessionIdUseCase: GetMessagesBySessionIdUseCase,
    private val startListeningForSpeechUseCase: StartListeningForSpeechUseCase,
    private val saveMessageUseCase: SaveMessageUseCase,
    private val generateContentWithContextUseCase: GenerateContentWithContextUseCase,
    private val createNewChatSessionUseCase: CreateNewChatSessionUseCase,
    private val speakTextUseCase: SpeakTextUseCase,
    private val cleanupResourcesUseCase: CleanupResourcesUseCase,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val sessionId = savedStateHandle.toRoute<ChatRoute>().sessionId

    private val _uiState = MutableStateFlow(ChatContract.UiState())
    val uiState: StateFlow<ChatContract.UiState> = _uiState
        .onStart {
            getMessagesBySessionId(sessionId)
            collectNetworkStatus()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = ChatContract.UiState()
        )

    fun onEvent(event: ChatContract.UiEvent) {
        when (event) {
            ChatContract.UiEvent.OnTheUserIsListened -> startListeningForSpeech()
            ChatContract.UiEvent.UserNotifiedTheError -> _uiState.update { it.copy(errorState = null) }
            ChatContract.UiEvent.OnShowMicrophonePermissionRationale -> _uiState.update {
                it.copy(
                    isRecordAudioPermissionRationaleInformationalDialogOpen = !it.isRecordAudioPermissionRationaleInformationalDialogOpen
                )
            }

            is ChatContract.UiEvent.OnUserAcceptOrDismissSnackbarInfo -> when (event.snackbarResult) {
                SnackbarResult.Dismissed -> _uiState.update { it.copy(speechResult = null) }
                SnackbarResult.ActionPerformed -> startListeningForSpeech()
            }
        }
    }

    private suspend fun getMessagesBySessionId(sessionId: String?) {
        sessionId ?: return

        val messages: List<ChatMessage> = getMessagesBySessionIdUseCase(sessionId = sessionId)

        _uiState.update { it.copy(messages = messages, currentSessionId = sessionId) }
    }

    private fun collectNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.getNetworkConnectivityFlow().collect {
                _uiState.update { state ->
                    state.copy(isNetworkAvailable = it)
                }
            }
        }
    }

    private fun startListeningForSpeech() {
        viewModelScope.launch {
            cleanupResourcesUseCase()

            startListeningForSpeechUseCase().collect { speechResult ->
                when (speechResult) {
                    is SpeechResult.FinalResult -> {
                        if (_uiState.value.messages.isEmpty()) {
                            val sessionId = createNewChatSessionUseCase()

                            _uiState.update {
                                it.copy(
                                    currentSessionId = sessionId,
                                    messages = emptyList()
                                )
                            }
                        }

                        saveMessageUseCase(_uiState.value.currentSessionId, ChatMessage(isFromUser = true, content = speechResult.text))

                        _uiState.update { state ->
                            state.copy(
                                speechResult = speechResult,
                                messages = state.messages + ChatMessage(isFromUser = true, content = speechResult.text)
                            )
                        }

                        getMessageWithContext(message = speechResult.text, chatHistory = _uiState.value.messages)
                    }

                    else -> _uiState.update { it.copy(speechResult = speechResult) }
                }
            }
        }
    }

    private fun getMessageWithContext(message: String, chatHistory: List<ChatMessage>) {
        viewModelScope.launch {
            generateContentWithContextUseCase(message = message, chatHistory = chatHistory).collect { response ->
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
                            speechResult = null
                        )
                    }

                    is Response.Success -> {
                        saveMessageUseCase(
                            sessionId = _uiState.value.currentSessionId,
                            message = ChatMessage(
                                isFromUser = false,
                                content = response.result
                            )
                        )

                        speakTextUseCase(text = response.result)

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

    fun cleanup() {
        cleanupResourcesUseCase()
    }

    override fun onCleared() {
        cleanupResourcesUseCase()

        super.onCleared()
    }
}
