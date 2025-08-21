package com.ahmetsirim.navigation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetsirim.chat.ChatRoute

internal fun NavGraphBuilder.setupChatUsageFlow() {
    composable<ChatRoute> {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Chat Route")
        }
    }
}