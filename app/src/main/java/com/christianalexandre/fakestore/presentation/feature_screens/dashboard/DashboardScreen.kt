package com.christianalexandre.fakestore.presentation.feature_screens.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.christianalexandre.fakestore.presentation.feature_screens.account.AccountScreen
import com.christianalexandre.fakestore.presentation.feature_screens.cart.CartScreen
import com.christianalexandre.fakestore.presentation.feature_screens.dashboard.components.BottomNavigationBar
import com.christianalexandre.fakestore.presentation.feature_screens.product_detail.ProductDetailsScreen
import com.christianalexandre.fakestore.presentation.feature_screens.products_list.ProductsListScreen
import com.christianalexandre.fakestore.presentation.navigation.graphs.ProductsGraph
import com.christianalexandre.fakestore.presentation.navigation.routes.AccountScreen
import com.christianalexandre.fakestore.presentation.navigation.routes.CartScreen
import com.christianalexandre.fakestore.presentation.navigation.routes.ProductDetailScreen
import com.christianalexandre.fakestore.presentation.navigation.routes.ProductsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavItems = listOf(ProductsScreen, CartScreen, AccountScreen)
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = currentRoute in bottomNavItems.map { it.route }
    val deepLinkUri = "app://fakestore/product_detail/{${ProductDetailScreen.ARG_PRODUCT_ID}}"
    val context = LocalContext.current

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    Log.d("PERMISSIONS", "Granted")
                } else {
                    Log.w("PERMISSIONS", "Not granted")
                }
            },
        )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

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
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ProductsGraph.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            navigation(
                startDestination = ProductsGraph.startDestination,
                route = ProductsGraph.route,
            ) {
                composable(ProductsScreen.route) {
                    ProductsListScreen(onProductClick = {
                        navController.navigate(ProductDetailScreen.createRoute(it.id))
                    })
                }

                composable(
                    ProductDetailScreen.route,
                    ProductDetailScreen.navArguments,
                    deepLinks =
                        listOf(
                            navDeepLink {
                                uriPattern = deepLinkUri
                                action = Intent.ACTION_VIEW
                            },
                        ),
                ) {
                    val productId = it.arguments?.getInt(ProductDetailScreen.ARG_PRODUCT_ID)
                    if (productId != null) {
                        ProductDetailsScreen(
                            onNavigateUp = { navController.navigateUp() },
                        )
                    }
                }
            }

            composable(CartScreen.route) {
                CartScreen()
            }

            composable(AccountScreen.route) {
                AccountScreen(onLogout = onLogout)
            }
        }
    }
}
