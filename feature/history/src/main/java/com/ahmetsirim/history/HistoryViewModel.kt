package com.ahmetsirim.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetsirim.domain.model.common.ErrorState
import com.ahmetsirim.domain.usecase.history.DeleteChatUseCase
import com.ahmetsirim.domain.usecase.history.GetAllChatHistoryUseCase
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
internal class HistoryViewModel @Inject constructor(
    private val getAllChatHistoryUseCase: GetAllChatHistoryUseCase,
    private val deleteChatUseCase: DeleteChatUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryContract.UiState())
    val uiState: StateFlow<HistoryContract.UiState> = _uiState
        .onStart { loadChatHistory() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryContract.UiState()
        )

    fun onEvent(event: HistoryContract.UiEvent) {
        when (event) {
            HistoryContract.UiEvent.ErrorNotified -> clearError()
            is HistoryContract.UiEvent.DeleteChat -> deleteChat(event.sessionId)
        }
    }

    private fun loadChatHistory() {
        viewModelScope.launch {
            getAllChatHistoryUseCase()
                .onSuccess { chatSessions ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            chatSessions = chatSessions.sortedByDescending { session -> session.updatedAt }
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorState = ErrorState(
                                exceptionMessageResId = com.ahmetsirim.designsystem.R.string.try_again,
                                exceptionSolutionSuggestionResId = com.ahmetsirim.designsystem.R.string.empty
                            )
                        )
                    }
                }
        }
    }

    private fun deleteChat(sessionId: String) {
        viewModelScope.launch {
            deleteChatUseCase(sessionId)
            loadChatHistory()
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorState = null) }
    }
}
