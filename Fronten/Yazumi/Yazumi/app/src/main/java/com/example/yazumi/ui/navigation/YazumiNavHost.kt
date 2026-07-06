package com.example.yazumi.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yazumi.data.AppContainer
import com.example.yazumi.ui.screens.auth.LoginScreen
import com.example.yazumi.ui.screens.auth.RegisterScreen
import com.example.yazumi.ui.screens.cart.CartScreen
import com.example.yazumi.ui.screens.catalog.CatalogScreen
import com.example.yazumi.ui.screens.catalog.CategoryProductsScreen
import com.example.yazumi.ui.screens.home.HomeScreen
import com.example.yazumi.ui.screens.orders.OrdersScreen
import com.example.yazumi.ui.screens.product.ProductDetailScreen
import com.example.yazumi.ui.viewmodel.AuthViewModel
import com.example.yazumi.ui.viewmodel.CartViewModel
import com.example.yazumi.ui.viewmodel.CatalogViewModel
import com.example.yazumi.ui.viewmodel.CategoryProductsViewModel
import com.example.yazumi.ui.viewmodel.HomeViewModel
import com.example.yazumi.ui.viewmodel.OrderHistoryViewModel
import com.example.yazumi.ui.viewmodel.ProductDetailViewModel
import com.example.yazumi.ui.viewmodel.ViewModelFactory

private val bottomNavRoutes = setOf(Routes.HOME, Routes.CATALOG, Routes.CART, Routes.ORDERS)

@Composable
fun YazumiNavHost(container: AppContainer) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val factory = remember(container) {
        ViewModelFactory(
            container.authRepository,
            container.productRepository,
            container.cartRepository,
            container.orderRepository,
        )
    }
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = isLoggedIn && currentRoute in bottomNavRoutes

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.HOME,
                        onClick = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") },
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.CATALOG,
                        onClick = {
                            navController.navigate(Routes.CATALOG) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Store, contentDescription = "Catálogo") },
                        label = { Text("Catálogo") },
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.CART,
                        onClick = {
                            navController.navigate(Routes.CART) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Pedido") },
                        label = { Text("Pedido") },
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.ORDERS,
                        onClick = {
                            navController.navigate(Routes.ORDERS) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Receipt, contentDescription = "Mis pedidos") },
                        label = { Text("Mis pedidos") },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Routes.HOME else Routes.LOGIN,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(Routes.HOME) {
                val homeViewModel: HomeViewModel = viewModel(factory = factory)
                HomeScreen(
                    viewModel = homeViewModel,
                    onProductClick = { navController.navigate(Routes.product(it)) },
                    onCategoryClick = { navController.navigate(Routes.category(it)) },
                    onLogout = {
                        authViewModel.logout()
                    },
                )
            }
            composable(Routes.CATALOG) {
                val catalogViewModel: CatalogViewModel = viewModel(factory = factory)
                CatalogScreen(
                    viewModel = catalogViewModel,
                    onCategoryClick = { navController.navigate(Routes.category(it)) },
                )
            }
            composable(
                route = Routes.CATEGORY,
                arguments = listOf(navArgument("marca") { type = NavType.StringType }),
            ) { backStackEntry ->
                val marca = backStackEntry.arguments?.getString("marca") ?: return@composable
                val categoryViewModel: CategoryProductsViewModel = viewModel(
                    factory = factory.categoryProducts(marca),
                )
                CategoryProductsScreen(
                    viewModel = categoryViewModel,
                    onProductClick = { navController.navigate(Routes.product(it)) },
                    onBack = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(Routes.CART) }
                )
            }
            composable(
                route = Routes.PRODUCT,
                arguments = listOf(navArgument("id") { type = NavType.IntType }),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                val productViewModel: ProductDetailViewModel = viewModel(
                    factory = factory.productDetail(id),
                )
                ProductDetailScreen(
                    viewModel = productViewModel,
                    snackbarHostState = snackbarHostState,
                    onBack = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(Routes.CART) }
                )
            }
            composable(Routes.CART) {
                val cartViewModel: CartViewModel = viewModel(factory = factory)
                CartScreen(viewModel = cartViewModel)
            }
            composable(Routes.ORDERS) {
                val ordersViewModel: OrderHistoryViewModel = viewModel(factory = factory)
                OrdersScreen(viewModel = ordersViewModel)
            }
        }
    }
}

