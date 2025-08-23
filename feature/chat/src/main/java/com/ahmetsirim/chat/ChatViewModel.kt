package com.ahmetsirim.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.common.Response
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
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
            is ChatContract.UiEvent.UserSendTheMessage -> getMessageWithContext(message = event.message, chatHistory = event.chatHistory)
            ChatContract.UiEvent.UserNotifiedTheError -> _uiState.update { it.copy(errorState = null) }
            ChatContract.UiEvent.OnShowMicrophonePermissionRationale -> _uiState.update { it.copy(isRecordAudioPermissionRationaleInformationalDialogOpen = !it.isRecordAudioPermissionRationaleInformationalDialogOpen) }
        }
    }

    private fun getMessageWithContext(message: String, chatHistory: List<ChatMessage>) {
        viewModelScope.launch {
            generativeAiModelRepository.generateContentWithContext(message = message, chatHistory = chatHistory).collect { response ->
                when (response) {
                    is Response.Error -> _uiState.update {
                        it.copy(
                            isAiTyping = false,
                            errorState = response.errorState
                        )
                    }

                    is Response.Loading -> _uiState.update {
                        it.copy(
                            errorState = null,
                            isAiTyping = true,
                            userInputMessage = "",
                            messages = it.messages + ChatMessage(isFromUser = true, content = message)
                        )
                    }

                    is Response.Success -> _uiState.update {
                        it.copy(
                            isAiTyping = false,
                            errorState = null,
                            userInputMessage = "",
                            messages = it.messages + ChatMessage(isFromUser = false, content = response.result)
                        )
                    }
                }
            }
        }
    }

}