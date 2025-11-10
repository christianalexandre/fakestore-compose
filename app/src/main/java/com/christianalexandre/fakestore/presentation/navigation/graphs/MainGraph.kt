package com.christianalexandre.fakestore.presentation.navigation.graphs

import com.christianalexandre.fakestore.presentation.navigation.routes.DashboardScreen

data object MainGraph {
    const val route = "main_graph"
    val startDestination = DashboardScreen.route
}