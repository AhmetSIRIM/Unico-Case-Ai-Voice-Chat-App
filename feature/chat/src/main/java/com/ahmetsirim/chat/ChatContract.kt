package com.ahmetsirim.chat

import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.SpeechResult
import com.ahmetsirim.domain.model.common.ErrorState

internal class ChatContract {

    data class UiState(
        val isAiSpeaking: Boolean = false,
        val errorState: ErrorState? = null,
        val speechResult: SpeechResult? = null,
        val messages: List<ChatMessage> = emptyList(),
        val isRecordAudioPermissionRationaleInformationalDialogOpen: Boolean = false,
    )

    sealed interface UiEvent {
        data class UserSendTheMessage(val message: String, val chatHistory: List<ChatMessage>) : UiEvent
        data object UserNotifiedTheError : UiEvent
        data object OnShowMicrophonePermissionRationale : UiEvent
        data object OnTheUserIsListened : UiEvent
    }

}