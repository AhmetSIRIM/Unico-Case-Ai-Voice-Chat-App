package com.ahmetsirim.chat

import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.common.ErrorState

internal class ChatContract {

    data class UiState(
        val isAiTyping: Boolean = false,
        val errorState: ErrorState? = null,

        val userInputMessage: String = "",
        val messages: List<ChatMessage> = emptyList(),

        val isRecordAudioPermissionRationaleInformationalDialogOpen: Boolean = false,
    )

    sealed interface UiEvent {
        data class UserSendTheMessage(val message: String, val chatHistory: List<ChatMessage>) : UiEvent
        data object UserNotifiedTheError : UiEvent
        data object OnShowMicrophonePermissionRationale : UiEvent
    }

}