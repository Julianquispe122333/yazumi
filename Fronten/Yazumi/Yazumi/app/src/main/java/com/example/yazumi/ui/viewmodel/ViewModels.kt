package com.example.yazumi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val isLoggedIn = authRepository.isLoggedIn.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false,
    )

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(telefono: String, codigo: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            authRepository.login(telefono, codigo)
                .onSuccess { _uiState.value = AuthUiState(isLoading = false, success = true) }
                .onFailure { _uiState.value = AuthUiState(isLoading = false, error = it.message) }
        }
    }

    fun register(nombres: String, telefono: String, direccion: String, negocio: String, codigo: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            authRepository.register(nombres, telefono, direccion, negocio.ifBlank { null }, codigo)
                .onSuccess { _uiState.value = AuthUiState(isLoading = false, success = true) }
                .onFailure { _uiState.value = AuthUiState(isLoading = false, error = it.message) }
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
            cartRepository.updateItem(productId, quantity)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        message = "Producto agregado al pedido",
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isAdding = false, error = it.message)
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

    fun confirmOrder(direccion: String, observaciones: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isConfirming = true, error = null)
            orderRepository.crearPedido(direccion, observaciones.ifBlank { null })
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isConfirming = false,
                        orderConfirmed = true,
                        confirmedPedido = it,
                        carrito = com.example.yazumi.data.model.Carrito(1, emptyList()),
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isConfirming = false, error = it.message)
                }
        }
    }

    fun resetConfirmation() {
        _uiState.value = _uiState.value.copy(orderConfirmed = false, confirmedPedido = null)
        loadCart()
    }
}

data class CartUiState(
    val isLoading: Boolean = false,
    val isConfirming: Boolean = false,
    val error: String? = null,
    val carrito: com.example.yazumi.data.model.Carrito = com.example.yazumi.data.model.Carrito(1, emptyList()),
    val orderConfirmed: Boolean = false,
    val confirmedPedido: com.example.yazumi.data.model.Pedido? = null,
)

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
