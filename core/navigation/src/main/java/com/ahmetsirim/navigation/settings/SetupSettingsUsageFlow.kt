package com.ahmetsirim.navigation.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetsirim.settings.SettingsContainer
import com.ahmetsirim.settings.SettingsRoute

internal fun NavGraphBuilder.setupSettingsUsageFlow(navController: NavController) {
    composable<SettingsRoute> {
        SettingsContainer(
            navigateUp = { navController.navigateUp() }
        )
    }
}
