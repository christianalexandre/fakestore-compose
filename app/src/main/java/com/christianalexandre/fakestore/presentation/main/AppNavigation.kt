package com.christianalexandre.fakestore.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.christianalexandre.fakestore.presentation.feature_screens.dashboard.DashboardScreen
import com.christianalexandre.fakestore.presentation.feature_screens.login.LoginScreen
import com.christianalexandre.fakestore.presentation.navigation.graphs.AuthGraph
import com.christianalexandre.fakestore.presentation.navigation.graphs.MainGraph
import com.christianalexandre.fakestore.presentation.navigation.routes.DashboardScreen
import com.christianalexandre.fakestore.presentation.navigation.routes.LoginScreen
import com.christianalexandre.fakestore.presentation.navigation.routes.ProductDetailScreen

@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (isLoggedIn) MainGraph.route else AuthGraph.route
    val deepLinkUri = "app://fakestore/product_detail/{${ProductDetailScreen.ARG_PRODUCT_ID}}"

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            startDestination = AuthGraph.startDestination,
            route = AuthGraph.route,
        ) {
            composable(LoginScreen.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(MainGraph.route) {
                            popUpTo(AuthGraph.route) { inclusive = true }
                        }
                    },
                )
            }
        }

        navigation(
            startDestination = MainGraph.startDestination,
            route = MainGraph.route,
            deepLinks = listOf(navDeepLink { uriPattern = deepLinkUri }),
        ) {
            composable(DashboardScreen.route) {
                DashboardScreen(
                    onLogout = {
                        navController.navigate(AuthGraph.route) {
                            popUpTo(MainGraph.route) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}
