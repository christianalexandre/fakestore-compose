package com.christianalexandre.fakestore.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Extends [NavigationRoute] to include properties needed for screens shown in the Bottom Navigation Bar.
 */
sealed interface BottomBarRoute : NavigationRoute {
    val labelResource: Int
    val icon: ImageVector
}