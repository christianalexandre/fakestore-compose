package com.christianalexandre.fakestore.presentation.dashboard

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.christianalexandre.fakestore.presentation.cart.CartScreen
import com.christianalexandre.fakestore.presentation.dashboard.components.BottomNavigationBar
import com.christianalexandre.fakestore.presentation.navigation.CartScreen
import com.christianalexandre.fakestore.presentation.navigation.ProductDetailScreen
import com.christianalexandre.fakestore.presentation.navigation.ProductsGraph
import com.christianalexandre.fakestore.presentation.navigation.ProductsScreen
import com.christianalexandre.fakestore.presentation.product_detail.ProductDetailsScreen
import com.christianalexandre.fakestore.presentation.products_list.ProductsListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavItems = listOf(ProductsScreen, CartScreen)
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    items = bottomNavItems,
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
            startDestination = ProductsGraph.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            navigation(
                startDestination = ProductsGraph.startDestination,
                route = ProductsGraph.route
            ) {
                composable(ProductsScreen.route) {
                    ProductsListScreen(onProductClick = {
                        navController.navigate(ProductDetailScreen.createRoute(it.id))
                    })
                }

                composable(
                    ProductDetailScreen.route,
                    ProductDetailScreen.navArguments
                ) {
                    val productId = it.arguments?.getInt(ProductDetailScreen.ARG_PRODUCT_ID)
                    if (productId != null) {
                        ProductDetailsScreen(
                            onNavigateUp = { navController.navigateUp() }
                        )
                    }
                }
            }

            composable(CartScreen.route) {
                CartScreen()
            }
        }
    }
}
