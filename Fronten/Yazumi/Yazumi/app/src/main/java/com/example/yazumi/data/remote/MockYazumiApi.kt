package com.example.yazumi.data.remote

import com.example.yazumi.data.model.ActualizarCarritoRequest
import com.example.yazumi.data.model.AgregarCarritoRequest
import com.example.yazumi.data.model.ApiResponse
import com.example.yazumi.data.model.Carrito
import com.example.yazumi.data.model.CarritoItem
import com.example.yazumi.data.model.CategoriaResponseCompat
import com.example.yazumi.data.model.CompraResponse
import com.example.yazumi.data.model.ComprarRequest
import com.example.yazumi.data.model.LoginRequest
import com.example.yazumi.data.model.Pedido
import com.example.yazumi.data.model.PedidoItem
import com.example.yazumi.data.model.Producto
import com.example.yazumi.data.model.RegisterRequest
import com.example.yazumi.data.model.RegistroResponse
import com.example.yazumi.data.model.Usuario
import kotlinx.coroutines.delay

/**
 * Implementación mock de YazumiApi para desarrollo/pruebas sin backend.
 * Se activa cuando BuildConfig.USE_MOCK_API = true.
 */
class MockYazumiApi : YazumiApi {

    private val mockUsuario = Usuario(
        idUsuario = 1,
        nombres = "Cliente Demo",
        telefono = "987654321",
        direccion = "Av. Principal 123",
        nombreNegocio = "Tienda Demo",
    )

    private val mockProductos = listOf(
        Producto(1, "LAY001", "Lays", "Caja Lays Clásicas", "Caja conteniendo 30 unidades de Papas Fritas Clásicas Lay's de 45g. Formato comercial listo para abastecer tu negocio.", "Caja de 30 un", 36.00, 50, "DISPONIBLE", null, true, 30, 1.50),
        Producto(2, "LAY002", "Lays", "Caja Lays Ondas Limón", "Caja conteniendo 30 unidades de Papas Fritas Lay's Ondas sabor Limón de 45g. Ideal para surtir los estantes de tu tienda.", "Caja de 30 un", 36.00, 45, "DISPONIBLE", null, true, 30, 1.50),
        Producto(3, "LAY003", "Lays", "Caja Lays BBQ", "Caja conteniendo 30 unidades de Papas Fritas Lay's sabor Barbacoa (BBQ) de 45g. Presentación mayorista.", "Caja de 30 un", 38.00, 40, "DISPONIBLE", null, true, 30, 1.60),
        Producto(4, "LAY004", "Lays", "Caja Lays Flamin Hot", "Caja conteniendo 30 unidades de Papas Fritas Lay's sabor Flamin Hot de 45g. Presentación mayorista.", "Caja de 30 un", 39.00, 35, "DISPONIBLE", null, true, 30, 1.70),
        Producto(5, "DOR001", "Doritos", "Caja Doritos Queso Mega", "Caja conteniendo 24 unidades de Tortillas de Maíz Doritos sabor Queso Mega de 50g. Snacks salados de alta rotación.", "Caja de 24 un", 36.00, 40, "DISPONIBLE", null, true, 24, 1.80),
        Producto(6, "DOR002", "Doritos", "Caja Doritos Flamin Hot", "Caja conteniendo 24 unidades de Tortillas de Maíz Doritos sabor Flamin Hot de 50g. Producto de alta rotación.", "Caja de 24 un", 38.00, 30, "DISPONIBLE", null, true, 24, 1.90),
        Producto(7, "CHE001", "Cheetos", "Caja Cheetos Crunchy", "Caja conteniendo 30 unidades de Chizitos Horneados Cheetos Crunchy sabor Queso de 40g. Ideal para la venta diaria en bodegas.", "Caja de 30 un", 30.00, 60, "DISPONIBLE", null, true, 30, 1.30),
        Producto(8, "CHTR001", "Cheese Tris", "Cinta Cheese Tris Original", "Tira/Cinta para colgar conteniendo 12 unidades de Cheese Tris sabor Queso de 35g. Maximiza espacio y visibilidad en tu local.", "Cinta de 12 un", 12.00, 80, "DISPONIBLE", null, true, 12, 1.20),
        Producto(9, "CUAT001", "Cuates", "Cinta Cuates Picantes", "Tira/Cinta para colgar conteniendo 12 unidades de Cacahuates Cuates Picantes de 40g. Producto listo para exhibir y vender.", "Cinta de 12 un", 14.40, 75, "DISPONIBLE", null, true, 12, 1.50),
        Producto(10, "RUF001", "Ruffles", "Caja Ruffles Original", "Caja conteniendo 24 unidades de Papas Fritas Onduladas Ruffles de 45g. Formato ideal para minimarkets y tiendas.", "Caja de 24 un", 31.20, 35, "DISPONIBLE", null, true, 24, 1.60),
    )

    private val carritoItems = mutableMapOf<Int, CarritoItem>()
    private val pedidos = mutableListOf<Pedido>()
    private var pedidoCounter = 1000

    // ─── Autenticación ────────────────────────────────────────────────────────

    override suspend fun login(request: LoginRequest): ApiResponse<Usuario> {
        delay(500)
        return if (request.telefono == "987654321" && request.password == "FRITOLAY2026") {
            ApiResponse(true, "Login exitoso", mockUsuario)
        } else {
            ApiResponse(false, "Credenciales inválidas. Usa 987654321 / FRITOLAY2026", null)
        }
    }

    override suspend fun register(request: RegisterRequest): ApiResponse<RegistroResponse> {
        delay(600)
        return ApiResponse(
            true,
            "Registro exitoso",
            RegistroResponse(2, request.nombres, request.telefono, request.direccion, request.nombreNegocio, null),
        )
    }

    // ─── Productos ────────────────────────────────────────────────────────────

    override suspend fun getProductos(marca: String?, busqueda: String?): ApiResponse<List<Producto>> {
        delay(300)
        val filtered = mockProductos.filter { p ->
            (marca == null || p.marca?.equals(marca, ignoreCase = true) == true) &&
                (busqueda == null || p.nombre.contains(busqueda, ignoreCase = true))
        }
        return ApiResponse(true, null, filtered)
    }

    override suspend fun getProducto(id: Int): ApiResponse<Producto> {
        delay(200)
        val p = mockProductos.find { it.idProducto == id }
        return if (p != null) ApiResponse(true, null, p) else ApiResponse(false, "Producto no encontrado", null)
    }

    override suspend fun getCategorias(): ApiResponse<List<CategoriaResponseCompat>> {
        delay(300)
        val cats = mockProductos
            .mapNotNull { it.marca }
            .distinct()
            .map { m -> CategoriaResponseCompat(m, null, mockProductos.count { it.marca == m }) }
        return ApiResponse(true, null, cats)
    }

    // ─── Carrito ──────────────────────────────────────────────────────────────

    override suspend fun getCarrito(idUsuario: Int): ApiResponse<Carrito> {
        delay(200)
        return ApiResponse(true, null, buildCarrito(idUsuario))
    }

    override suspend fun agregarAlCarrito(request: AgregarCarritoRequest): ApiResponse<Carrito> {
        delay(200)
        val producto = mockProductos.find { it.idProducto == request.idProducto }
            ?: return ApiResponse(false, "Producto no encontrado", null)
        val existing = carritoItems[request.idProducto]
        val newQty = (existing?.cantidad ?: 0) + request.cantidad
        carritoItems[request.idProducto] = CarritoItem(
            idProducto = producto.idProducto,
            nombreProducto = producto.nombre,
            imagen = producto.imagen,
            cantidad = newQty,
            precioUnitario = producto.precio,
            subtotal = newQty * producto.precio,
        )
        return ApiResponse(true, null, buildCarrito(request.idUsuario))
    }

    override suspend fun actualizarCarritoItem(request: ActualizarCarritoRequest): ApiResponse<Carrito> {
        delay(200)
        val producto = mockProductos.find { it.idProducto == request.idProducto }
            ?: return ApiResponse(false, "Producto no encontrado", null)
        if (request.cantidad <= 0) {
            carritoItems.remove(request.idProducto)
        } else {
            carritoItems[request.idProducto] = CarritoItem(
                idProducto = producto.idProducto,
                nombreProducto = producto.nombre,
                imagen = producto.imagen,
                cantidad = request.cantidad,
                precioUnitario = producto.precio,
                subtotal = request.cantidad * producto.precio,
            )
        }
        return ApiResponse(true, null, buildCarrito(request.idUsuario))
    }

    override suspend fun eliminarDelCarrito(idUsuario: Int, idProducto: Int): ApiResponse<Carrito> {
        delay(200)
        carritoItems.remove(idProducto)
        return ApiResponse(true, null, buildCarrito(idUsuario))
    }

    override suspend fun vaciarCarrito(idUsuario: Int): ApiResponse<Carrito> {
        delay(200)
        carritoItems.clear()
        return ApiResponse(true, null, buildCarrito(idUsuario))
    }

    override suspend fun comprar(idUsuario: Int, request: ComprarRequest): ApiResponse<CompraResponse> {
        delay(600)
        if (carritoItems.isEmpty()) {
            return ApiResponse(false, "El carrito está vacío", null)
        }
        val items = carritoItems.values.map {
            PedidoItem(it.idProducto, it.nombreProducto, it.cantidad, it.precioUnitario, it.subtotal)
        }
        val total = items.sumOf { it.subtotal }
        val idPedido = ++pedidoCounter
        val compra = CompraResponse(
            idPedido = idPedido,
            idUsuario = idUsuario,
            estado = "Registrado",
            fechaCompra = "2026-07-01",
            direccionEntrega = request.direccionEntrega,
            detalle = items,
            total = total,
        )
        // Guardar en historial mock
        pedidos.add(
            0,
            Pedido(idPedido, idUsuario, "2026-07-01", "Registrado", request.direccionEntrega, items, total),
        )
        carritoItems.clear()
        return ApiResponse(true, "Compra realizada", compra)
    }

    // ─── Pedidos ──────────────────────────────────────────────────────────────

    override suspend fun getPedidosPorUsuario(idUsuario: Int): ApiResponse<List<Pedido>> {
        delay(300)
        return ApiResponse(true, null, pedidos.toList())
    }

    override suspend fun getPedido(idPedido: Int): ApiResponse<Pedido> {
        delay(200)
        val p = pedidos.find { it.idPedido == idPedido }
        return if (p != null) ApiResponse(true, null, p) else ApiResponse(false, "Pedido no encontrado", null)
    }

    private fun buildCarrito(idUsuario: Int = 1) = Carrito(
        idCarrito = 1,
        idUsuario = idUsuario,
        items = carritoItems.values.toList(),
    )
}
