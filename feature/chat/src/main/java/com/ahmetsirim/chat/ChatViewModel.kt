package com.ahmetsirim.chat

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetsirim.common.prompt.PromptsUsedThroughoutTheApplication
import com.ahmetsirim.domain.model.ChatMessage
import com.ahmetsirim.domain.model.common.Response
import com.ahmetsirim.domain.repository.GenerativeAiModelRepository
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
internal class ChatViewModel @Inject constructor(
    private val generativeAiModelRepository: GenerativeAiModelRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatContract.UiState())
    val uiState: StateFlow<ChatContract.UiState> = _uiState
        .onStart { getInitiativeCommunicationProposal() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = ChatContract.UiState()
        )

    fun onEvent(event: ChatContract.UiEvent) {
        when (event) {
            is ChatContract.UiEvent.UserAcceptTheProposal -> startTheConversationWithProposal(initiativeCommunicationProposal = event.initiativeCommunicationProposal)
            is ChatContract.UiEvent.UserInputMessageChanged -> _uiState.update { it.copy(userInputMessage = event.userInputMessage, errorState = null) }
            is ChatContract.UiEvent.UserSendTheMessage -> getMessageWithContext(message = event.message, chatHistory = event.chatHistory)
            ChatContract.UiEvent.UserIgnoreTheProposal -> _uiState.update { it.copy(oneSentenceInitiativeCommunicationProposal = "", errorState = null) }
        }
    }

    private fun getInitiativeCommunicationProposal() {
        viewModelScope.launch {
            generativeAiModelRepository.getMessage(
                message = PromptsUsedThroughoutTheApplication.INITIATIVE_COMMUNICATION_PROPOSAL_PROMPT
            ).collect { response ->
                when (response) {
                    is Response.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorState = response.errorState
                        )
                    }

                    is Response.Loading -> _uiState.update {
                        it.copy(
                            errorState = null,
                            isLoading = true
                        )
                    }

                    is Response.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorState = null,
                            oneSentenceInitiativeCommunicationProposal = response.result
                        )
                    }
                }
            }
        }
    }

    private fun startTheConversationWithProposal(initiativeCommunicationProposal: String) {
        viewModelScope.launch {
            generativeAiModelRepository.getMessageWithContext(
                message = PromptsUsedThroughoutTheApplication.getInitiativeCommunicationProposalPrompt(initiativeCommunicationProposal),
                chatHistory = listOf(
                    ChatMessage(isFromUser = false, content = _uiState.value.oneSentenceInitiativeCommunicationProposal),
                )
            ).collect { response ->
                when (response) {
                    is Response.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorState = response.errorState
                        )
                    }

                    is Response.Loading -> _uiState.update {
                        it.copy(
                            errorState = null,
                            isLoading = true,
                            oneSentenceInitiativeCommunicationProposal = ""
                        )
                    }

                    is Response.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorState = null,
                            messages = (it.messages + ChatMessage(isFromUser = false, content = response.result)).toMutableStateList()
                        )
                    }
                }
            }
        }
    }


    private fun getMessageWithContext(message: String, chatHistory: List<ChatMessage>) {
        viewModelScope.launch {
            generativeAiModelRepository.getMessageWithContext(message = message, chatHistory = chatHistory).collect { response ->
                when (response) {
                    is Response.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorState = response.errorState
                        )
                    }

                    is Response.Loading -> _uiState.update {
                        it.copy(
                            errorState = null,
                            isLoading = true,
                            userInputMessage = "",
                            oneSentenceInitiativeCommunicationProposal = "",
                            messages = (it.messages + ChatMessage(isFromUser = true, content = message)).toMutableStateList()
                        )
                    }

                    is Response.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorState = null,
                            userInputMessage = "",
                            messages = (it.messages + ChatMessage(isFromUser = false, content = response.result)).toMutableStateList()
                        )
                    }
                }
            }
        }
    }

}