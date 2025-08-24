package com.ahmetsirim.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatContainer(
    navigateToSettings: () -> Unit,
    navigateToHistory: () -> Unit
) {
    val viewModel: ChatViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreen(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) },
        navigateToSettings = {
            navigateToSettings()
            viewModel.resetTheUiState()
        },
        navigateToHistory = {
            navigateToHistory()
            viewModel.resetTheUiState()
        }
    )
}
