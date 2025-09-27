package com.christianalexandre.fakestore.presentation.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.christianalexandre.fakestore.R

sealed class Screen(val route: String, val labelResource: Int, val icon: ImageVector) {
    object Products : Screen("products", R.string.home, Icons.Filled.Home)
    object ProductsDetail : Screen("product_detail", R.string.products, Icons.Filled.Home)
    object Cart : Screen("cart", R.string.cart, Icons.Filled.ShoppingCart)
}