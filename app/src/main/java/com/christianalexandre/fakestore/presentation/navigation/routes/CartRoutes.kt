package com.christianalexandre.fakestore.presentation.navigation.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import com.christianalexandre.fakestore.R
import com.christianalexandre.fakestore.presentation.navigation.BottomBarRoute

data object CartScreen : BottomBarRoute {
    override val route = "cart"
    override val labelResource = R.string.cart
    override val icon = Icons.Filled.ShoppingCart
}