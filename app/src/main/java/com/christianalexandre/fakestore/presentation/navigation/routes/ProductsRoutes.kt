package com.christianalexandre.fakestore.presentation.navigation.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.christianalexandre.fakestore.R
import com.christianalexandre.fakestore.presentation.navigation.BottomBarRoute
import com.christianalexandre.fakestore.presentation.navigation.NavigationRoute

data object ProductsScreen : BottomBarRoute {
    override val route = "products"
    override val labelResource = R.string.home
    override val icon = Icons.Filled.Home
}

data object ProductDetailScreen : NavigationRoute {
    const val ARG_PRODUCT_ID = "productId"
    override val route = "product_detail/{$ARG_PRODUCT_ID}"
    override val navArguments = listOf(navArgument(ARG_PRODUCT_ID) { type = NavType.IntType })

    fun createRoute(productId: Int) = "product_detail/$productId"
}