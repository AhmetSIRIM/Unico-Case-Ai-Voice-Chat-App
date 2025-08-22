package com.ahmetsirim.chat

import com.ahmetsirim.domain.model.common.ErrorState

internal class ChatContract {

    data class UiState(
        val isLoading: Boolean = false,
        val errorState: ErrorState? = null,
    )

    sealed interface UiEvent

}