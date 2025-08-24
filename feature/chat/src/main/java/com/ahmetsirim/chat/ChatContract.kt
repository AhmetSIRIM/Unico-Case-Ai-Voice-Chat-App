package com.ahmetsirim.chat

import androidx.compose.material3.SnackbarResult
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.SpeechResult
import com.ahmetsirim.domain.model.common.ErrorState

internal class ChatContract {

    data class UiState(
        val currentSessionId: String = "",
        val isAiSpeaking: Boolean = false,
        val isNetworkAvailable: Boolean = true,
        val errorState: ErrorState? = null,
        val speechResult: SpeechResult? = null,
        val messages: List<ChatMessage> = emptyList(),
        val isRecordAudioPermissionRationaleInformationalDialogOpen: Boolean = false,
    )

    sealed interface UiEvent {
        data object UserNotifiedTheError : UiEvent
        data object OnShowMicrophonePermissionRationale : UiEvent
        data object OnTheUserIsListened : UiEvent
        data class OnUserAcceptOrDismissSnackbarInfo(val snackbarResult: SnackbarResult) : UiEvent
    }
}
