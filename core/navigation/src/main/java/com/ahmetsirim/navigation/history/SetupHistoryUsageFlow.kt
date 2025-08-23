package com.ahmetsirim.navigation.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetsirim.chat.ChatRoute
import com.ahmetsirim.history.HistoryContainer
import com.ahmetsirim.history.HistoryRoute

internal fun NavGraphBuilder.setupHistoryUsageFlow(navController: NavController) {
    composable<HistoryRoute> {
        HistoryContainer(
            navigateUp = { navController.navigateUp() },
            navigateToChat = { sessionId ->
                navController.navigate(
                    ChatRoute(sessionId),
                    builder = {
                        popUpTo(ChatRoute::class) {
                            inclusive = true
                        }
                    }
                )
            }
        )
    }
}
