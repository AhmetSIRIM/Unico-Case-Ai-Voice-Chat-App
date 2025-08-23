package com.ahmetsirim.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetsirim.domain.model.common.ErrorState
import com.ahmetsirim.domain.repository.LocalChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HistoryViewModel @Inject constructor(
    private val localChatRepository: LocalChatRepository,
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
            HistoryContract.UiEvent.LoadChatHistory -> loadChatHistory()
            HistoryContract.UiEvent.ErrorNotified -> clearError()
            is HistoryContract.UiEvent.DeleteChat -> deleteChat(event.sessionId)
            is HistoryContract.UiEvent.NavigateToChat -> {
                // Navigation will be handled in the Container/Screen
            }
        }
    }

    private fun loadChatHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            localChatRepository.getAllChatHistory()
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
            localChatRepository.deleteChat(sessionId)
            // Refresh the list after deletion
            loadChatHistory()
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorState = null) }
    }
}