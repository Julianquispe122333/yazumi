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
        Producto(1, "LAY001", "Lays", "Cinta Lays Clásicas", "Cinta conteniendo 12 unidades de Papas Fritas Clásicas Lay's de 45g. Formato listo para colgar y vender.", "Cinta de 12 un", 14.40, 50, "DISPONIBLE", null, true, 12, 1.50),
        Producto(2, "LAY002", "Lays", "Cinta Lays Ondas Limón", "Cinta conteniendo 12 unidades de Papas Fritas Lay's Ondas sabor Limón de 45g. Ideal para exhibición en colgantes.", "Cinta de 12 un", 14.40, 45, "DISPONIBLE", null, true, 12, 1.50),
        Producto(3, "LAY003", "Lays", "Cinta Lays BBQ", "Cinta conteniendo 12 unidades de Papas Fritas Lay's sabor Barbacoa (BBQ) de 45g. Ideal para colgar y vender.", "Cinta de 12 un", 14.40, 40, "DISPONIBLE", null, true, 12, 1.60),
        Producto(4, "LAY004", "Lays", "Cinta Lays Flamin Hot", "Cinta conteniendo 12 unidades de Papas Fritas Lay's sabor Flamin Hot de 45g. Formato listo para colgar y vender.", "Cinta de 12 un", 14.40, 35, "DISPONIBLE", null, true, 12, 1.70),
        Producto(5, "DOR001", "Doritos", "Cinta Doritos Queso Mega", "Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Queso Mega de 50g. Snacks listos para exhibir.", "Cinta de 12 un", 18.00, 40, "DISPONIBLE", null, true, 12, 1.80),
        Producto(6, "DOR002", "Doritos", "Cinta Doritos Flamin Hot", "Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Flamin Hot de 50g. Formato listo para colgar y vender.", "Cinta de 12 un", 18.00, 30, "DISPONIBLE", null, true, 12, 1.90),
        Producto(7, "CHE001", "Cheetos", "Cinta Cheetos Crunchy", "Cinta conteniendo 12 unidades de Chizitos Horneados Cheetos Crunchy sabor Queso de 40g. Excelente rotación en colgadores.", "Cinta de 12 un", 13.20, 60, "DISPONIBLE", null, true, 12, 1.30),
        Producto(8, "CHTR001", "Cheese Tris", "Cinta Cheese Tris Original", "Tira/Cinta para colgar conteniendo 12 unidades de Cheese Tris sabor Queso de 35g. Maximiza espacio y visibilidad en tu local.", "Cinta de 12 un", 12.00, 80, "DISPONIBLE", null, true, 12, 1.20),
        Producto(9, "CUAT001", "Cuates", "Cinta Cuates Picantes", "Tira/Cinta para colgar conteniendo 12 unidades de Cacahuates Cuates Picantes de 40g. Producto listo para exhibir y vender.", "Cinta de 12 un", 14.40, 75, "DISPONIBLE", null, true, 12, 1.50),
        Producto(10, "RUF001", "Ruffles", "Cinta Ruffles Original", "Cinta conteniendo 12 unidades de Papas Fritas Onduladas Ruffles de 45g. Formato colgante ideal para tiendas.", "Cinta de 12 un", 15.60, 35, "DISPONIBLE", null, true, 12, 1.60),
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
