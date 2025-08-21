package com.ahmetsirim.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ahmetsirim.chat.ChatRoute
import com.ahmetsirim.common.log.log
import com.ahmetsirim.navigation.bottombar.topLevelRoutes
import com.ahmetsirim.navigation.chat.setupChatUsageFlow
import com.ahmetsirim.navigation.history.setupHistoryUsageFlow
import com.ahmetsirim.navigation.settings.setupSettingsUsageFlow

/**
 * Main navigation host for the UnicoCaseAiVoiceChat application.
 *
 * This composable serves as the central navigation hub for the entire application,
 * implementing a modular navigation structure that coordinates between different feature modules.
 *
 * Each feature module has its own navigation setup which is connected here to create
 * a cohesive navigation experience. The navigation structure uses type-safe route objects
 * instead of string routes for improved maintainability.
 *
 * The Routes classes are deliberately placed within each feature module along with the
 * Navigation Compose dependency implementation in each feature module. This architecture
 * allows each feature to access navigation arguments directly using the
 * [SavedStateHandle.toRoute] function, maintaining proper encapsulation and
 * feature independence while still enabling type-safe navigation argument access.
 *
 * @param modifier Modifier to be applied to the NavHost composable
 */
@Composable
fun UnicoCaseAiVoiceChatNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController().apply {

        if (!BuildConfig.DEBUG) return@apply

        @SuppressLint("RestrictedApi")
        currentBackStack
            .collectAsStateWithLifecycle()
            .value
            .map { it.destination.route?.substringAfterLast('.') }
            .also { infoLog -> log(message = infoLog.toString(), level = Log.DEBUG) } // tag:"NavHostController"
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val context = LocalContext.current

                topLevelRoutes.forEach { topLevelRoute ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = topLevelRoute.icon,
                                contentDescription = context.getString(topLevelRoute.nameResourceId)
                            )
                        },
                        label = { Text(context.getString(topLevelRoute.nameResourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true,
                        onClick = {
                            navController.navigate(topLevelRoute.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = ChatRoute
        ) {
            setupChatUsageFlow()
            setupHistoryUsageFlow()
            setupSettingsUsageFlow()
        }
    }
}