package com.christianalexandre.fakestore.presentation.navigation.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import com.christianalexandre.fakestore.R
import com.christianalexandre.fakestore.presentation.navigation.BottomBarRoute

data object AccountScreen : BottomBarRoute {
    override val route = "account"
    override val labelResource = R.string.account
    override val icon = Icons.Filled.AccountCircle
}