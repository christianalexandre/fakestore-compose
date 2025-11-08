package com.christianalexandre.fakestore.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.christianalexandre.fakestore.presentation.dashboard.DashboardScreen
import com.christianalexandre.fakestore.presentation.login.LoginScreen
import com.christianalexandre.fakestore.presentation.navigation.graphs.AuthGraph
import com.christianalexandre.fakestore.presentation.navigation.graphs.MainGraph
import com.christianalexandre.fakestore.presentation.navigation.routes.DashboardScreen
import com.christianalexandre.fakestore.presentation.navigation.routes.LoginScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val startDestination =
        if (FirebaseAuth.getInstance().currentUser != null) {
            MainGraph.route
        } else {
            AuthGraph.route
        }

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
        ) {
            composable(DashboardScreen.route) {
                DashboardScreen(
                    onLogout = {
                        navController.navigate(AuthGraph.route) {
                            popUpTo(MainGraph.route) { inclusive = true }
                        }
                        FirebaseAuth.getInstance().signOut()
                    },
                )
            }
        }
    }
}
