package com.ahmetsirim.navigation.bottombar

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

internal data class TopLevelRoute<T : Any>(
    @param:StringRes val nameResourceId: Int,
    val route: T,
    val icon: ImageVector,
)