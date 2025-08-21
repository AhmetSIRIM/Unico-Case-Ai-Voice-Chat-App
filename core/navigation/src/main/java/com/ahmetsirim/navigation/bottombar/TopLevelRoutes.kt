package com.ahmetsirim.navigation.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import com.ahmetsirim.chat.ChatRoute
import com.ahmetsirim.designsystem.R
import com.ahmetsirim.history.HistoryRoute
import com.ahmetsirim.settings.SettingsRoute

internal val topLevelRoutes = listOf(
    TopLevelRoute(R.string.chat_route, ChatRoute, Icons.Filled.Call),
    TopLevelRoute(R.string.history_route, HistoryRoute, Icons.Filled.Email),
    TopLevelRoute(R.string.settings_route, SettingsRoute, Icons.Filled.Settings)
)