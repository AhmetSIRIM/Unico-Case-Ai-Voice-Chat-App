package com.ahmetsirim.navigation.chat

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetsirim.chat.ChatContainer
import com.ahmetsirim.chat.ChatRoute

internal fun NavGraphBuilder.setupChatUsageFlow() {
    composable<ChatRoute> {
        ChatContainer()
    }
}