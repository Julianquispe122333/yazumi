package com.example.yazumi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yazumi.data.model.CompraResponse
import com.example.yazumi.data.repository.AuthRepository
import com.example.yazumi.data.repository.CartRepository
import com.example.yazumi.data.repository.OrderRepository
import com.example.yazumi.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ─── Auth ─────────────────────────────────────────────────────────────────────

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val isLoggedIn = authRepository.isLoggedIn.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false,
    )

    val currentUser = authRepository.currentUser.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null,
    )

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(telefono: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, success = false)
            authRepository.login(telefono, password)
                .onSuccess { _uiState.value = AuthUiState(isLoading = false, success = true) }
                .onFailure { _uiState.value = AuthUiState(isLoading = false, error = it.message) }
        }
    }

    fun register(
        codigoValidacion: String,
        nombres: String,
        telefono: String,
        password: String,
        direccion: String,
        negocio: String,
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, success = false)
            authRepository.register(
                codigoValidacion = codigoValidacion,
                nombres = nombres,
                telefono = telefono,
                password = password,
                direccion = direccion,
                nombreNegocio = negocio.ifBlank { null },
            )
                .onSuccess { _uiState.value = AuthUiState(isLoading = false, success = true) }
                .onFailure { _uiState.value = AuthUiState(isLoading = false, error = it.message) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState(success = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)

// ─── Home ─────────────────────────────────────────────────────────────────────

class HomeViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val promos = productRepository.getPromociones()
            val favoritos = productRepository.getFavoritos()
            val categorias = productRepository.getCategorias()

            if (promos.isSuccess && favoritos.isSuccess && categorias.isSuccess) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    promociones = promos.getOrDefault(emptyList()),
                    favoritos = favoritos.getOrDefault(emptyList()),
                    categorias = categorias.getOrDefault(emptyList()),
                )
            } else {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    error = promos.exceptionOrNull()?.message
                        ?: favoritos.exceptionOrNull()?.message
                        ?: categorias.exceptionOrNull()?.message,
                )
            }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val promociones: List<com.example.yazumi.data.model.Promocion> = emptyList(),
    val favoritos: List<com.example.yazumi.data.model.Producto> = emptyList(),
    val categorias: List<com.example.yazumi.data.model.Categoria> = emptyList(),
)

// ─── Catalog ──────────────────────────────────────────────────────────────────

class CatalogViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState(isLoading = true))
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadCategorias()
    }

    fun loadCategorias() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            productRepository.getCategorias()
                .onSuccess { _uiState.value = CatalogUiState(categorias = it) }
                .onFailure { _uiState.value = CatalogUiState(error = it.message) }
        }
    }
}

data class CatalogUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val categorias: List<com.example.yazumi.data.model.Categoria> = emptyList(),
)

// ─── CategoryProducts ─────────────────────────────────────────────────────────

class CategoryProductsViewModel(
    private val productRepository: ProductRepository,
    private val marca: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryProductsUiState(isLoading = true))
    val uiState: StateFlow<CategoryProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            productRepository.getProductos(marca = marca)
                .onSuccess { _uiState.value = CategoryProductsUiState(marca = marca, productos = it) }
                .onFailure { _uiState.value = CategoryProductsUiState(marca = marca, error = it.message) }
        }
    }
}

data class CategoryProductsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val marca: String = "",
    val productos: List<com.example.yazumi.data.model.Producto> = emptyList(),
)

// ─── ProductDetail ────────────────────────────────────────────────────────────

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val productId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState(isLoading = true))
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            productRepository.getProducto(productId)
                .onSuccess { _uiState.value = ProductDetailUiState(producto = it) }
                .onFailure { _uiState.value = ProductDetailUiState(error = it.message) }
        }
    }

    fun addToCart(quantity: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAdding = true, message = null)
            // Intenta agregar primero; si falla, intenta actualizar
            cartRepository.agregarItem(productId, quantity)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        message = "Producto agregado al pedido",
                    )
                }
                .onFailure {
                    // Si ya existe el item, intentar actualizar
                    cartRepository.updateItem(productId, quantity)
                        .onSuccess {
                            _uiState.value = _uiState.value.copy(
                                isAdding = false,
                                message = "Cantidad actualizada en el pedido",
                            )
                        }
                        .onFailure { err ->
                            _uiState.value = _uiState.value.copy(isAdding = false, error = err.message)
                        }
                }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val isAdding: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val producto: com.example.yazumi.data.model.Producto? = null,
)

// ─── Cart ─────────────────────────────────────────────────────────────────────

class CartViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val currentUser = authRepository.currentUser.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null,
    )

    private val _uiState = MutableStateFlow(CartUiState(isLoading = true))
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            cartRepository.getCarrito()
                .onSuccess { _uiState.value = _uiState.value.copy(isLoading = false, carrito = it) }
                .onFailure { _uiState.value = _uiState.value.copy(isLoading = false, error = it.message) }
        }
    }

    fun updateQuantity(productoId: Int, cantidad: Int) {
        viewModelScope.launch {
            cartRepository.updateItem(productoId, cantidad)
                .onSuccess { _uiState.value = _uiState.value.copy(carrito = it) }
                .onFailure { _uiState.value = _uiState.value.copy(error = it.message) }
        }
    }

    fun removeItem(productoId: Int) {
        viewModelScope.launch {
            cartRepository.eliminarItem(productoId)
                .onSuccess { _uiState.value = _uiState.value.copy(carrito = it) }
                .onFailure { _uiState.value = _uiState.value.copy(error = it.message) }
        }
    }

    fun confirmOrder(direccion: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isConfirming = true, error = null)
            cartRepository.comprar(direccion)
                .onSuccess { compra ->
                    _uiState.value = _uiState.value.copy(
                        isConfirming = false,
                        orderConfirmed = true,
                        confirmedCompra = compra,
                        carrito = com.example.yazumi.data.model.Carrito(1, items = emptyList()),
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isConfirming = false, error = it.message)
                }
        }
    }

    fun resetConfirmation() {
        _uiState.value = _uiState.value.copy(orderConfirmed = false, confirmedCompra = null)
        loadCart()
    }
}

data class CartUiState(
    val isLoading: Boolean = false,
    val isConfirming: Boolean = false,
    val error: String? = null,
    val carrito: com.example.yazumi.data.model.Carrito = com.example.yazumi.data.model.Carrito(1, items = emptyList()),
    val orderConfirmed: Boolean = false,
    val confirmedCompra: CompraResponse? = null,
)

// ─── OrderHistory ─────────────────────────────────────────────────────────────

class OrderHistoryViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderHistoryUiState(isLoading = true))
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    init {
        loadPedidos()
    }

    fun loadPedidos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            orderRepository.getPedidos()
                .onSuccess { _uiState.value = OrderHistoryUiState(pedidos = it) }
                .onFailure { _uiState.value = OrderHistoryUiState(error = it.message) }
        }
    }
}

data class OrderHistoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val pedidos: List<com.example.yazumi.data.model.Pedido> = emptyList(),
)

// ─── AdminViewModel ───────────────────────────────────────────────────────────

class AdminViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState(isLoading = true))
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        loadPedidos()
    }

    fun loadPedidos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            orderRepository.getTodosLosPedidos()
                .onSuccess { pedidos ->
                    _uiState.value = AdminUiState(
                        isLoading = false,
                        pedidosTotales = pedidos,
                        pedidosActivos = pedidos.filter { 
                            it.estado == "Registrado" || it.estado == "En preparación" || it.estado == "En camino"
                        }
                    )
                }
                .onFailure {
                    _uiState.value = AdminUiState(isLoading = false, error = it.message)
                }
        }
    }

    fun cambiarEstado(idPedido: Int, idEstado: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true)
            orderRepository.actualizarEstadoPedido(idPedido, idEstado)
                .onSuccess {
                    loadPedidos()
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isUpdating = false, error = it.message)
                }
        }
    }
}

data class AdminUiState(
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val error: String? = null,
    val pedidosTotales: List<com.example.yazumi.data.model.Pedido> = emptyList(),
    val pedidosActivos: List<com.example.yazumi.data.model.Pedido> = emptyList(),
)

// ─── ViewModelFactory ─────────────────────────────────────────────────────────

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(authRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(productRepository) as T
            modelClass.isAssignableFrom(CatalogViewModel::class.java) ->
                CatalogViewModel(productRepository) as T
            modelClass.isAssignableFrom(CartViewModel::class.java) ->
                CartViewModel(cartRepository, orderRepository, authRepository) as T
            modelClass.isAssignableFrom(OrderHistoryViewModel::class.java) ->
                OrderHistoryViewModel(orderRepository) as T
            modelClass.isAssignableFrom(AdminViewModel::class.java) ->
                AdminViewModel(orderRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }

    fun categoryProducts(marca: String) = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CategoryProductsViewModel(productRepository, marca) as T
        }
    }

    fun productDetail(productId: Int) = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductDetailViewModel(productRepository, cartRepository, productId) as T
        }
    }
}

