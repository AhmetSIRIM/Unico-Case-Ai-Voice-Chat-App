package com.ahmetsirim.navigation.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetsirim.chat.ChatContainer
import com.ahmetsirim.chat.ChatRoute
import com.ahmetsirim.settings.SettingsRoute

internal fun NavGraphBuilder.setupChatUsageFlow(navController: NavController) {
    composable<ChatRoute> {
        ChatContainer(navigateToSettings = { navController.navigate(SettingsRoute) })
    }
}