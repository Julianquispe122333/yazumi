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
import com.example.yazumi.ui.screens.admin.AdminScreen
import com.example.yazumi.ui.viewmodel.AdminViewModel
import com.example.yazumi.ui.viewmodel.AuthViewModel
import com.example.yazumi.ui.viewmodel.CartViewModel
import com.example.yazumi.ui.viewmodel.CatalogViewModel
import com.example.yazumi.ui.viewmodel.CategoryProductsViewModel
import com.example.yazumi.ui.viewmodel.HomeViewModel
import com.example.yazumi.ui.viewmodel.OrderHistoryViewModel
import com.example.yazumi.ui.viewmodel.ProductDetailViewModel
import com.example.yazumi.ui.viewmodel.ViewModelFactory

// Rutas que muestran la barra de navegación inferior
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
    val currentUser by authViewModel.currentUser.collectAsState(initial = null)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // ─── Guardia de sesión ────────────────────────────────────────────────────
    // startDestination es siempre Routes.LOGIN (fijo), la redirección se hace aquí.
    LaunchedEffect(isLoggedIn, currentUser) {
        when {
            !isLoggedIn -> {
                // Si no está autenticado, enviar a login limpiando todo el backstack
                if (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }
            currentUser?.esAdmin == true -> {
                // Admin: sólo redirigir si aún está en pantallas de auth o usuario normal
                if (currentRoute == Routes.LOGIN || currentRoute == Routes.REGISTER ||
                    currentRoute in bottomNavRoutes
                ) {
                    navController.navigate(Routes.ADMIN_DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }
            currentUser != null -> {
                // Usuario normal: redirigir desde login/registro/admin a home
                if (currentRoute == Routes.LOGIN || currentRoute == Routes.REGISTER ||
                    currentRoute == Routes.ADMIN_DASHBOARD
                ) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }
        }
    }

    val showBottomBar = isLoggedIn && currentUser?.esAdmin != true && currentRoute in bottomNavRoutes

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.HOME,
                        onClick = {
                            navController.navigate(Routes.HOME) {
                                // HOME es el primer destino de usuario, pop hasta él y reusar
                                popUpTo(Routes.HOME) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") },
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.CATALOG,
                        onClick = {
                            navController.navigate(Routes.CATALOG) {
                                popUpTo(Routes.HOME) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Store, contentDescription = "Catálogo") },
                        label = { Text("Catálogo") },
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.CART,
                        onClick = {
                            navController.navigate(Routes.CART) {
                                popUpTo(Routes.HOME) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Pedido") },
                        label = { Text("Pedido") },
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.ORDERS,
                        onClick = {
                            navController.navigate(Routes.ORDERS) {
                                popUpTo(Routes.HOME) { inclusive = false }
                                launchSingleTop = true
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
            // startDestination SIEMPRE fijo en LOGIN — la redirección la hace el LaunchedEffect
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(innerPadding),
        ) {
            // ─── Auth ─────────────────────────────────────────────────────────
            composable(Routes.LOGIN) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        val user = authViewModel.currentUser.value
                        val destination = if (user?.esAdmin == true) Routes.ADMIN_DASHBOARD else Routes.HOME
                        navController.navigate(destination) {
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
                        val user = authViewModel.currentUser.value
                        val destination = if (user?.esAdmin == true) Routes.ADMIN_DASHBOARD else Routes.HOME
                        navController.navigate(destination) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            // ─── Pantallas de usuario (con bottom nav) ────────────────────────
            composable(Routes.HOME) {
                val homeViewModel: HomeViewModel = viewModel(factory = factory)
                HomeScreen(
                    viewModel = homeViewModel,
                    onProductClick = { navController.navigate(Routes.product(it)) },
                    onCategoryClick = { navController.navigate(Routes.category(it)) },
                    onLogout = { authViewModel.logout() },
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
                val encodedMarca = backStackEntry.arguments?.getString("marca") ?: return@composable
                val marca = android.net.Uri.decode(encodedMarca)
                val categoryViewModel: CategoryProductsViewModel = viewModel(
                    factory = factory.categoryProducts(marca),
                )
                CategoryProductsScreen(
                    viewModel = categoryViewModel,
                    onProductClick = { navController.navigate(Routes.product(it)) },
                    onBack = { navController.popBackStack() },
                    onNavigateToCart = {
                        // Navegar al carrito sin acumular en el backstack
                        navController.navigate(Routes.CART) {
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
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
                    onNavigateToCart = {
                        navController.navigate(Routes.CART) {
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.CART) {
                val cartViewModel: CartViewModel = viewModel(factory = factory)
                // Determinar si hay una pantalla anterior a la que regresar
                // (distinta de HOME — si prev es HOME no necesita flecha atrás)
                val prevRoute = navController.previousBackStackEntry?.destination?.route
                val showBack = prevRoute != null && prevRoute != Routes.HOME
                CartScreen(
                    viewModel = cartViewModel,
                    onNavigateToCatalog = {
                        navController.navigate(Routes.CATALOG) {
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onBack = if (showBack) {
                        { navController.popBackStack() }
                    } else null,
                )
            }
            composable(Routes.ORDERS) {
                val ordersViewModel: OrderHistoryViewModel = viewModel(factory = factory)
                OrdersScreen(viewModel = ordersViewModel)
            }

            // ─── Admin ────────────────────────────────────────────────────────
            composable(Routes.ADMIN_DASHBOARD) {
                val adminViewModel: AdminViewModel = viewModel(factory = factory)
                AdminScreen(
                    viewModel = adminViewModel,
                    onLogout = { authViewModel.logout() }
                )
            }
        }
    }
}
