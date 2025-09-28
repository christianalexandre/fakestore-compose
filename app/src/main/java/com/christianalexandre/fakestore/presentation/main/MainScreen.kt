package com.christianalexandre.fakestore.presentation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.christianalexandre.fakestore.presentation.cart.CartScreen
import com.christianalexandre.fakestore.presentation.main.components.BottomNavigationBar
import com.christianalexandre.fakestore.presentation.main.navigation.Screen
import com.christianalexandre.fakestore.presentation.product_detail.ProductDetailsScreen
import com.christianalexandre.fakestore.presentation.products_list.ProductsListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = currentRoute in listOf(Screen.Products.route, Screen.Cart.route)

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    items = listOf(Screen.Products, Screen.Cart),
                    currentRoute = currentRoute,
                    onItemClick = {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "products_graph",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            navigation(startDestination = Screen.Products.route, route = "products_graph") {
                composable(Screen.Products.route) {
                    ProductsListScreen(onProductClick = {
                        navController.navigate(Screen.ProductsDetail.route + "/${it.id}")
                    })
                }

                composable(
                    Screen.ProductsDetail.route + "/{productId}",
                    listOf(navArgument("productId") { type = NavType.IntType })
                ) {
                    val productId = it.arguments?.getInt("productId")
                    if (productId != null) {
                        ProductDetailsScreen(
                            productId = productId,
                            onNavigateUp = { navController.navigateUp() }
                        )
                    }
                }
            }

            composable(Screen.Cart.route) {
                CartScreen()
            }
        }
    }
}
