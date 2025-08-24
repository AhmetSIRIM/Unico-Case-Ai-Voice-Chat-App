package com.ahmetsirim.history

import com.ahmetsirim.domain.model.common.ErrorState
import com.ahmetsirim.domain.model.db.ChatSession

internal class HistoryContract {

    data class UiState(
        val isLoading: Boolean = true,
        val chatSessions: List<ChatSession> = emptyList(),
        val errorState: ErrorState? = null
    )

    sealed interface UiEvent {
        data class DeleteChat(val sessionId: String) : UiEvent
        data object ErrorNotified : UiEvent
    }
}
