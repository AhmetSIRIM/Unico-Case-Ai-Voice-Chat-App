package com.ahmetsirim.navigation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetsirim.settings.SettingsRoute

internal fun NavGraphBuilder.setupSettingsUsageFlow() {
    composable<SettingsRoute> {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Settings Route")
        }
    }
}