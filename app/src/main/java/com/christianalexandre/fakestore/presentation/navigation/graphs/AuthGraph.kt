package com.christianalexandre.fakestore.presentation.navigation.graphs

import com.christianalexandre.fakestore.presentation.navigation.routes.LoginScreen

data object AuthGraph {
    const val route = "auth_graph"
    val startDestination = LoginScreen.route
}