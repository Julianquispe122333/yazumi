package com.example.yazumi.data.remote

import com.example.yazumi.data.ProductImages
import com.example.yazumi.data.model.ApiResponse
import com.example.yazumi.data.model.Carrito
import com.example.yazumi.data.model.CarritoItem
import com.example.yazumi.data.model.Categoria
import com.example.yazumi.data.model.CrearPedidoRequest
import com.example.yazumi.data.model.LoginRequest
import com.example.yazumi.data.model.Pedido
import com.example.yazumi.data.model.PedidoItem
import com.example.yazumi.data.model.Producto
import com.example.yazumi.data.model.Promocion
import com.example.yazumi.data.model.RegisterRequest
import com.example.yazumi.data.model.Usuario
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicInteger

class MockYazumiApi : YazumiApi {

    private val codigoValidacion = "FRITOLAY2026"
    private var currentUser: Usuario? = null
    private val carritoItems = mutableMapOf<Int, CarritoItem>()
    private val pedidos = mutableListOf<Pedido>()
    private val pedidoIdCounter = AtomicInteger(1000)

    private val productos = listOf(
        Producto(1, "LAY-001", "Lays Clásicas", "Papas fritas crujientes", "45g", 3.50, 120, ProductImages.forProduct(1, "Lays"), "Lays"),
        Producto(2, "LAY-002", "Lays Limón", "Sabor limón y sal", "45g", 3.50, 85, ProductImages.forProduct(2, "Lays"), "Lays"),
        Producto(3, "LAY-003", "Lays BBQ", "Sabor barbacoa ahumada", "45g", 3.80, 60, ProductImages.forProduct(3, "Lays"), "Lays"),
        Producto(4, "LAY-004", "Lays Flamin Hot", "Picante intenso", "45g", 3.90, 40, ProductImages.forProduct(4, "Lays"), "Lays"),
        Producto(5, "DOR-001", "Doritos Nacho", "Tortilla con queso nacho", "62g", 4.20, 95, ProductImages.forProduct(5, "Doritos"), "Doritos"),
        Producto(6, "DOR-002", "Doritos Flamin Hot", "Nacho picante", "62g", 4.50, 70, ProductImages.forProduct(6, "Doritos"), "Doritos"),
        Producto(7, "DOR-003", "Doritos Dinamita", "Explosión de chile", "55g", 4.20, 55, ProductImages.forProduct(7, "Doritos"), "Doritos"),
        Producto(8, "CHE-001", "Cheetos Crunchy", "Queso crujiente", "50g", 3.20, 100, ProductImages.forProduct(8, "Cheetos"), "Cheetos"),
        Producto(9, "CHE-002", "Cheetos Bolitas", "Bolitas de queso", "50g", 3.20, 90, ProductImages.forProduct(9, "Cheetos"), "Cheetos"),
        Producto(10, "CHE-003", "Cheetos Flamin Hot", "Bolitas picantes", "50g", 3.50, 75, ProductImages.forProduct(10, "Cheetos"), "Cheetos"),
        Producto(11, "CUA-001", "Cuates Naturales", "Cacahuates salados", "100g", 2.80, 110, ProductImages.forProduct(11, "Cuates"), "Cuates"),
        Producto(12, "CUA-002", "Cuates Enchilados", "Cacahuates con chile", "100g", 2.80, 80, ProductImages.forProduct(12, "Cuates"), "Cuates"),
        Producto(13, "CHT-001", "Cheese Tris Original", "Snack de maíz con queso", "55g", 2.50, 130, ProductImages.forProduct(13, "Cheese Tris"), "Cheese Tris"),
        Producto(14, "CHT-002", "Cheese Tris Hot", "Queso con chile", "55g", 2.80, 65, ProductImages.forProduct(14, "Cheese Tris"), "Cheese Tris"),
    )

    private val promociones = listOf(
        Promocion(1, "2x1 en Lays", "Lleva 2 bolsas de Lays Clásicas al precio de 1", null, 50, 0xFFFFD100),
        Promocion(2, "Combo Doritos", "3 bolsas Doritos Nacho por S/ 10.00", null, 15, 0xFFE31837),
        Promocion(3, "Semana Cheetos", "10% de descuento en toda la línea Cheetos", null, 10, 0xFF003DA5),
    )

    override suspend fun login(request: LoginRequest): ApiResponse<Usuario> {
        delay(600)
        if (request.codigoValidacion != codigoValidacion) {
            return ApiResponse(false, "Código de validación incorrecto", null)
        }
        val user = Usuario(
            idUsuario = 1,
            nombres = "Cliente Demo",
            telefono = request.telefono,
            direccion = "Zona 10, Ciudad de Guatemala",
            nombreNegocio = "Tienda El Buen Snack",
        )
        currentUser = user
        return ApiResponse(true, null, user)
    }

    override suspend fun register(request: RegisterRequest): ApiResponse<Usuario> {
        delay(800)
        if (request.codigoValidacion != codigoValidacion) {
            return ApiResponse(false, "Código de validación incorrecto", null)
        }
        val user = Usuario(
            idUsuario = 2,
            nombres = request.nombres,
            telefono = request.telefono,
            direccion = request.direccion,
            nombreNegocio = request.nombreNegocio,
        )
        currentUser = user
        return ApiResponse(true, "Registro exitoso", user)
    }

    override suspend fun getProductos(marca: String?, busqueda: String?): ApiResponse<List<Producto>> {
        delay(400)
        var result = productos
        if (!marca.isNullOrBlank()) {
            result = result.filter { it.marca.equals(marca, ignoreCase = true) }
        }
        if (!busqueda.isNullOrBlank()) {
            result = result.filter {
                it.nombre.contains(busqueda, ignoreCase = true) ||
                    it.marca.contains(busqueda, ignoreCase = true)
            }
        }
        return ApiResponse(true, null, result)
    }

    override suspend fun getProducto(id: Int): ApiResponse<Producto> {
        delay(300)
        val producto = productos.find { it.idProducto == id }
        return if (producto != null) {
            ApiResponse(true, null, producto)
        } else {
            ApiResponse(false, "Producto no encontrado", null)
        }
    }

    override suspend fun getCategorias(): ApiResponse<List<Categoria>> {
        delay(300)
        val categorias = productos
            .groupBy { it.marca }
            .map { (marca, items) ->
                Categoria(
                    nombre = marca,
                    imagen = ProductImages.forBrand(marca),
                    cantidadProductos = items.size,
                )
            }
            .sortedBy { it.nombre }
        return ApiResponse(true, null, categorias)
    }

    override suspend fun getFavoritos(): ApiResponse<List<Producto>> {
        delay(300)
        val favoritos = listOf(
            productos[0],
            productos[4],
            productos[7],
            productos[12],
        )
        return ApiResponse(true, null, favoritos)
    }

    override suspend fun getPromociones(): ApiResponse<List<Promocion>> {
        delay(300)
        return ApiResponse(true, null, promociones)
    }

    override suspend fun getCarrito(): ApiResponse<Carrito> {
        delay(300)
        return ApiResponse(true, null, buildCarrito())
    }

    override suspend fun updateCarritoItem(productoId: Int, cantidad: Int): ApiResponse<Carrito> {
        delay(300)
        val producto = productos.find { it.idProducto == productoId }
            ?: return ApiResponse(false, "Producto no encontrado", null)

        if (cantidad <= 0) {
            carritoItems.remove(productoId)
        } else if (cantidad > producto.stock) {
            return ApiResponse(false, "Stock insuficiente (disponible: ${producto.stock})", null)
        } else {
            carritoItems[productoId] = CarritoItem(
                idProducto = producto.idProducto,
                nombre = producto.nombre,
                imagen = producto.imagen,
                cantidad = cantidad,
                precioUnitario = producto.precio,
                stock = producto.stock,
            )
        }
        return ApiResponse(true, null, buildCarrito())
    }

    override suspend fun crearPedido(request: CrearPedidoRequest): ApiResponse<Pedido> {
        delay(800)
        if (carritoItems.isEmpty()) {
            return ApiResponse(false, "El carrito está vacío", null)
        }
        val items = carritoItems.values.map {
            PedidoItem(
                nombre = it.nombre,
                cantidad = it.cantidad,
                precioUnitario = it.precioUnitario,
                subtotal = it.subtotal,
            )
        }
        val total = items.sumOf { it.subtotal }
        val pedido = Pedido(
            idPedido = pedidoIdCounter.incrementAndGet(),
            fechaPedido = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
            total = total,
            direccionEntrega = request.direccionEntrega,
            estado = "Pendiente",
            items = items,
        )
        pedidos.add(0, pedido)
        carritoItems.clear()
        return ApiResponse(true, "Pedido registrado correctamente", pedido)
    }

    override suspend fun getPedidos(): ApiResponse<List<Pedido>> {
        delay(400)
        return ApiResponse(true, null, pedidos.toList())
    }

    private fun buildCarrito() = Carrito(
        idCarrito = 1,
        items = carritoItems.values.toList(),
    )
}
