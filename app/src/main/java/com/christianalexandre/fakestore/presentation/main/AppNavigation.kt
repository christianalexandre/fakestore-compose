package com.christianalexandre.fakestore.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.christianalexandre.fakestore.presentation.dashboard.DashboardScreen
import com.christianalexandre.fakestore.presentation.login.LoginScreen
import com.christianalexandre.fakestore.presentation.navigation.graphs.AuthGraph
import com.christianalexandre.fakestore.presentation.navigation.graphs.MainGraph
import com.christianalexandre.fakestore.presentation.navigation.routes.DashboardScreen
import com.christianalexandre.fakestore.presentation.navigation.routes.LoginScreen
import com.christianalexandre.fakestore.presentation.navigation.routes.ProductDetailScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var isLoading by remember { mutableStateOf(true) }
    var currentUser by remember { mutableStateOf<FirebaseUser?>(null) }
    val deepLinkUri = "app://fakestore/product_detail/{${ProductDetailScreen.ARG_PRODUCT_ID}}"

    DisposableEffect(Unit) {
        val authListener =
            FirebaseAuth.AuthStateListener { firebaseAuth ->
                currentUser = firebaseAuth.currentUser
                isLoading = false
            }
        FirebaseAuth.getInstance().addAuthStateListener(authListener)

        onDispose {
            FirebaseAuth.getInstance().removeAuthStateListener(authListener)
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        val startDestination =
            if (currentUser != null) {
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
                deepLinks =
                    listOf(
                        navDeepLink {
                            uriPattern = deepLinkUri
                        },
                    ),
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
}
