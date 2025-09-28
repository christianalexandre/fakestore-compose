package com.christianalexandre.fakestore.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import com.christianalexandre.fakestore.R

data object CartScreen : BottomBarRoute {
    override val route = "cart"
    override val labelResource = R.string.cart
    override val icon = Icons.Filled.ShoppingCart
}