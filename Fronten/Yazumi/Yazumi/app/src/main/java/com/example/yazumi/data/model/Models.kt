package com.example.yazumi.data.model

data class Usuario(
    val idUsuario: Int,
    val nombres: String,
    val telefono: String,
    val direccion: String,
    val nombreNegocio: String?,
    val estado: Int = 1,
)

data class Producto(
    val idProducto: Int,
    val codigoProducto: String?,
    val nombre: String,
    val descripcion: String?,
    val presentacion: String?,
    val precio: Double,
    val stock: Int,
    val imagen: String?,
    val marca: String,
)

data class Categoria(
    val nombre: String,
    val imagen: String?,
    val cantidadProductos: Int,
)

data class Promocion(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val imagen: String?,
    val descuentoPorcentaje: Int,
    val colorHex: Long,
)

data class CarritoItem(
    val idProducto: Int,
    val nombre: String,
    val imagen: String?,
    val cantidad: Int,
    val precioUnitario: Double,
    val stock: Int,
) {
    val subtotal: Double get() = cantidad * precioUnitario
}

data class Carrito(
    val idCarrito: Int,
    val items: List<CarritoItem>,
) {
    val total: Double get() = items.sumOf { it.subtotal }
    val cantidadItems: Int get() = items.sumOf { it.cantidad }
}

data class Pedido(
    val idPedido: Int,
    val fechaPedido: String,
    val total: Double,
    val direccionEntrega: String,
    val estado: String,
    val items: List<PedidoItem>,
)

data class PedidoItem(
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,
)

data class LoginRequest(
    val telefono: String,
    val codigoValidacion: String,
)

data class RegisterRequest(
    val nombres: String,
    val telefono: String,
    val direccion: String,
    val nombreNegocio: String?,
    val codigoValidacion: String,
)

data class CrearPedidoRequest(
    val direccionEntrega: String,
    val observaciones: String?,
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
)
