package com.example.yazumi.data.model

// ─── Usuario ──────────────────────────────────────────────────────────────────
data class Usuario(
    val idUsuario: Int,
    val nombres: String,
    val telefono: String,
    val direccion: String,
    val nombreNegocio: String?,
    val estado: Int = 1,
    val esAdmin: Boolean = false,
)

// ─── Producto ─────────────────────────────────────────────────────────────────
data class Producto(
    val idProducto: Int,
    val codigoProducto: String?,
    val marca: String?,
    val nombre: String,
    val descripcion: String?,
    val presentacion: String?,
    val precio: Double,
    val stock: Int,
    val stockEstado: String?,   // "DISPONIBLE" o "SIN STOCK"
    val imagen: String?,
    val activo: Boolean? = true,
    val unidadesPorPaquete: Int = 1,
    val precioSugerido: Double = 0.0,
)

// ─── Categoria ────────────────────────────────────────────────────────────────
data class Categoria(
    val nombre: String,
    val imagen: String?,
    val cantidadProductos: Int,
)

// ─── Promocion (mock, no existe en backend aún) ───────────────────────────────
data class Promocion(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val imagen: String?,
    val descuentoPorcentaje: Int,
    val colorHex: Long,
)

// ─── Carrito ──────────────────────────────────────────────────────────────────
data class CarritoItem(
    val idDetalle: Int? = null,
    val idProducto: Int,
    val nombreProducto: String,
    val imagen: String?,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,
    val stock: Int = 0,
) {
    // Alias de compatibilidad con componentes existentes
    val nombre: String get() = nombreProducto
}

data class Carrito(
    val idCarrito: Int,
    val idUsuario: Int = 0,
    val items: List<CarritoItem>,
    val totalItems: Int = items.sumOf { it.cantidad },
    val totalGeneral: Double = items.sumOf { it.subtotal },
) {
    val total: Double get() = totalGeneral
    val cantidadItems: Int get() = totalItems
}

// ─── Pedido (historial) ───────────────────────────────────────────────────────
data class Pedido(
    val idPedido: Int,
    val idUsuario: Int = 0,
    val fechaPedido: String,
    val estado: String,
    val direccionEntrega: String,
    val detalle: List<PedidoItem>,
    val total: Double,
) {
    val items: List<PedidoItem> get() = detalle
}

data class PedidoItem(
    val idProducto: Int? = null,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,
) {
    // Alias de compatibilidad
    val nombre: String get() = nombreProducto
}

// ─── Compra (respuesta al confirmar carrito) ──────────────────────────────────
data class CompraResponse(
    val idPedido: Int,
    val idUsuario: Int = 0,
    val estado: String,
    val fechaCompra: String,
    val direccionEntrega: String,
    val detalle: List<PedidoItem>,
    val total: Double,
)

// ─── Requests de autenticación ────────────────────────────────────────────────
data class LoginRequest(
    val telefono: String,
    val password: String,
)

data class RegisterRequest(
    val codigoValidacion: String,
    val nombres: String,
    val telefono: String,
    val password: String,
    val direccion: String,
    val nombreNegocio: String?,
)

// ─── Requests de carrito ──────────────────────────────────────────────────────
data class AgregarCarritoRequest(
    val idUsuario: Int,
    val idProducto: Int,
    val cantidad: Int,
)

data class ActualizarCarritoRequest(
    val idUsuario: Int,
    val idProducto: Int,
    val cantidad: Int,
)

data class ComprarRequest(
    val direccionEntrega: String,
)

// ─── Requests de pedido ───────────────────────────────────────────────────────
// (por compatibilidad, aunque ya no se usa directamente)
data class CrearPedidoRequest(
    val direccionEntrega: String,
)

// ─── Wrapper de respuesta API ─────────────────────────────────────────────────
data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
)

// ─── Tipos de respuesta del backend (compatibilidad) ─────────────────────────

/** Respuesta del endpoint GET /api/productos/categorias */
data class CategoriaResponseCompat(
    val nombre: String,
    val imagen: String?,
    val cantidadProductos: Int,
) {
    fun toCategoria() = Categoria(nombre = nombre, imagen = imagen, cantidadProductos = cantidadProductos)
}

/** Respuesta del endpoint POST /api/usuarios/registrar */
data class RegistroResponse(
    val idUsuario: Int,
    val nombres: String,
    val telefono: String,
    val direccion: String,
    val nombreNegocio: String?,
    val fechaRegistro: String?,
) {
    fun toUsuario() = Usuario(
        idUsuario = idUsuario,
        nombres = nombres,
        telefono = telefono,
        direccion = direccion,
        nombreNegocio = nombreNegocio,
    )
}

