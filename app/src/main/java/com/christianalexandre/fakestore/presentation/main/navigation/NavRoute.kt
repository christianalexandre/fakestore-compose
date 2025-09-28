package com.christianalexandre.fakestore.presentation.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.christianalexandre.fakestore.R

/**
 * Defines the contract for any navigable screen in the app.
 */
sealed interface NavRoute {
    val route: String
    val navArguments: List<NamedNavArgument> get() = emptyList()
}

/**
 * Extends [NavRoute] to include properties needed for screens shown in the Bottom Navigation Bar.
 */
sealed interface BottomBarRoute : NavRoute {
    val labelResource: Int
    val icon: ImageVector
}

data object ProductsScreen : BottomBarRoute {
    override val route = "products"
    override val labelResource = R.string.home
    override val icon = Icons.Filled.Home
}

data object CartScreen : BottomBarRoute {
    override val route = "cart"
    override val labelResource = R.string.cart
    override val icon = Icons.Filled.ShoppingCart
}

data object ProductDetailScreen : NavRoute {
    const val ARG_PRODUCT_ID = "productId"
    override val route = "product_detail/{$ARG_PRODUCT_ID}"
    override val navArguments = listOf(navArgument(ARG_PRODUCT_ID) { type = NavType.IntType })

    fun createRoute(productId: Int) = "product_detail/$productId"
}

/**
 * Defines the contract for a navigation graph.
 */
data object ProductsGraph {
    const val route = "products_graph"
    val startDestination = ProductsScreen.route
}
