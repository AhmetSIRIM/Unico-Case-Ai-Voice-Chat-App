package com.ahmetsirim.chat

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.common.ErrorState

internal class ChatContract {

    data class UiState(
        val isLoading: Boolean = false,
        val errorState: ErrorState? = null,

        val oneSentenceInitiativeCommunicationProposal: String = "",
        val isUserAcceptTheProposal: Boolean = false,

        val userInputMessage: String = "",

        val messages: SnapshotStateList<ChatMessage> = emptyList<ChatMessage>().toMutableStateList(),
    )

    sealed interface UiEvent {
        data class UserInputMessageChanged(val userInputMessage: String) : UiEvent
        data class UserAcceptTheProposal(val initiativeCommunicationProposal: String) : UiEvent
        data object UserIgnoreTheProposal : UiEvent
        data class UserSendTheMessage(val message: String, val chatHistory: List<ChatMessage>) : UiEvent
    }

}