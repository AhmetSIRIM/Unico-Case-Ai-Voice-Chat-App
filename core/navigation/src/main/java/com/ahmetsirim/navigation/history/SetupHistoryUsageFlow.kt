package com.ahmetsirim.navigation.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ahmetsirim.history.HistoryRoute

internal fun NavGraphBuilder.setupHistoryUsageFlow() {
    composable<HistoryRoute> {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("History Route")
        }
    }
}