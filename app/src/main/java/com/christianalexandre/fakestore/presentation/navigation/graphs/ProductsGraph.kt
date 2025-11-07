package com.christianalexandre.fakestore.presentation.navigation.graphs

import com.christianalexandre.fakestore.presentation.navigation.routes.ProductsScreen

data object ProductsGraph {
    const val route = "products_graph"
    val startDestination = ProductsScreen.route
}