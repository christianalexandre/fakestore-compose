package com.christianalexandre.fakestore.presentation.navigation

import androidx.navigation.NamedNavArgument

/**
 * Defines the contract for any navigable screen in the app.
 */
sealed interface NavigationRoute {
    val route: String
    val navArguments: List<NamedNavArgument> get() = emptyList()
}
