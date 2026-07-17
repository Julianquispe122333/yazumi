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

    private val mockAdmin = Usuario(
        idUsuario = 999,
        nombres = "Administrador",
        telefono = "999999999",
        direccion = "Oficina Principal",
        nombreNegocio = "Yazumi Admin",
        esAdmin = true,
    )

    private val mockProductos = listOf(
        Producto(1, "LAYS001", "Lays", "Cinta Lays Clásicas", "Cinta conteniendo 12 unidades de Papas Fritas Clásicas Lay's de 45g. Formato listo para colgar y vender.", "Cinta de 12 un", 14.40, 50, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fc/Lays_packet.jpg/320px-Lays_packet.jpg", true, 12, 1.50),
        Producto(2, "LAYS002", "Lays", "Cinta Lays Ondas Limón", "Cinta conteniendo 12 unidades de Papas Fritas Lay's Ondas sabor Limón de 45g. Ideal para exhibición en colgantes.", "Cinta de 12 un", 14.40, 45, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Lays_Sour_Cream_%26_Onion.jpg/320px-Lays_Sour_Cream_%26_Onion.jpg", true, 12, 1.50),
        Producto(3, "LAYS003", "Lays", "Cinta Lays BBQ", "Cinta conteniendo 12 unidades de Papas Fritas Lay's sabor Barbacoa (BBQ) de 45g. Sabor ahumado intenso.", "Cinta de 12 un", 14.40, 40, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Potato_chips_with_barbecue_flavor.jpg/320px-Potato_chips_with_barbecue_flavor.jpg", true, 12, 1.60),
        Producto(4, "LAYS004", "Lays", "Cinta Lays Spicy", "Cinta conteniendo 12 unidades de Papas Fritas Lay's sabor Picante de 45g. Nivel de picante medio-alto.", "Cinta de 12 un", 15.00, 30, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Lays_Spicy.jpg/320px-Lays_Spicy.jpg", true, 12, 1.70),
        Producto(5, "DORI001", "Doritos", "Cinta Doritos Queso Mega", "Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Queso Mega de 50g. Snacks crujientes e irresistibles.", "Cinta de 12 un", 18.00, 40, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Doritos_Nacho_Cheese.jpg/320px-Doritos_Nacho_Cheese.jpg", true, 12, 1.80),
        Producto(6, "DORI002", "Doritos Flamin Hot", "Cinta Doritos Flamin Hot", "Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Flamin Hot de 50g. Picante extremo para valientes.", "Cinta de 12 un", 19.20, 35, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Doritos_Flamin%27_Hot.jpg/320px-Doritos_Flamin%27_Hot.jpg", true, 12, 2.00),
        Producto(7, "DORI003", "Doritos", "Cinta Doritos Pizza", "Cinta conteniendo 12 unidades de Tortillas de Maíz Doritos sabor Pizza de 50g. Combinación perfecta de queso y especias.", "Cinta de 12 un", 18.00, 25, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Doritos_Pizza.jpg/320px-Doritos_Pizza.jpg", true, 12, 1.85),
        Producto(8, "CHEE001", "Cheetos", "Cinta Cheetos Crunchy", "Cinta conteniendo 12 unidades de Chizitos Horneados Cheetos Crunchy sabor Queso de 40g. La forma divertida de comer queso.", "Cinta de 12 un", 13.20, 60, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Cheetos.jpg/320px-Cheetos.jpg", true, 12, 1.30),
        Producto(9, "CHEE002", "Cheetos", "Cinta Cheetos Puffs", "Cinta conteniendo 12 unidades de Snacks Horneados inflados Cheetos sabor Queso de 40g. Textura suave y aireada.", "Cinta de 12 un", 13.20, 45, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Cheetos_Puffs.jpg/320px-Cheetos_Puffs.jpg", true, 12, 1.30),
        Producto(10, "CHEE003", "Cheetos", "Cinta Cheetos Flamin Hot", "Cinta conteniendo 12 unidades de Cheetos Horneados sabor Flamin Hot de 40g. Suaves pero muy picantes.", "Cinta de 12 un", 13.20, 40, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Baked_Cheetos_Flamin_Hot.jpg/320px-Baked_Cheetos_Flamin_Hot.jpg", true, 12, 1.30),
        Producto(11, "CHTR001", "Cheese Tris", "Cinta Cheese Tris Original", "Tira/Cinta para colgar conteniendo 12 unidades de Cheese Tris sabor Queso de 35g. Un clásico infaltable.", "Cinta de 12 un", 12.00, 80, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Cheese_puffs.jpg/320px-Cheese_puffs.jpg", true, 12, 1.20),
        Producto(12, "CHTR002", "Cheese Tris", "Cinta Cheese Tris Picante", "Tira/Cinta conteniendo 12 unidades de Cheese Tris sabor Queso Picante de 35g. Toque picante en cada bocado.", "Cinta de 12 un", 12.60, 50, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/Spicy_cheese_puffs.jpg/320px-Spicy_cheese_puffs.jpg", true, 12, 1.25),
        Producto(13, "CUAT001", "Cuates", "Cinta Cuates Picantes", "Tira/Cinta para colgar conteniendo 12 unidades de Cacahuates Cuates Picantes de 40g. Maní crujiente sabor picante y limón.", "Cinta de 12 un", 14.40, 75, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Roasted_peanuts.jpg/320px-Roasted_peanuts.jpg", true, 12, 1.50),
        Producto(14, "CUAT002", "Cuates", "Cinta Cuates Salados", "Tira/Cinta para colgar conteniendo 12 unidades de Cacahuates Cuates con Sal de 40g. Maní clásico tostado saladito.", "Cinta de 12 un", 14.40, 65, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Salted_peanuts.jpg/320px-Salted_peanuts.jpg", true, 12, 1.50),
        Producto(15, "RUF001", "Ruffles", "Cinta Ruffles Original", "Cinta conteniendo 12 unidades de Papas Fritas Onduladas Ruffles de 45g. Ondas que retienen más sabor.", "Cinta de 12 un", 15.60, 35, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Ruffles_potato_chips_bag.jpg/320px-Ruffles_potato_chips_bag.jpg", true, 12, 1.60),
        Producto(16, "RUF002", "Ruffles", "Cinta Ruffles Queso", "Cinta conteniendo 12 unidades de Papas Fritas Onduladas sabor Queso Cheddar Ruffles de 45g. Extra quesosas.", "Cinta de 12 un", 15.60, 30, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Ruffles_Cheddar_and_Sour_Cream_Chips_Bag.jpg/320px-Ruffles_Cheddar_and_Sour_Cream_Chips_Bag.jpg", true, 12, 1.60),
        Producto(17, "KARI001", "Karinto", "Cinta Karinto Confitado", "Tira/Cinta conteniendo 12 unidades de Maní Confitado Karinto de 50g. Maní dulce tostado artesanal.", "Cinta de 12 un", 12.00, 55, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Praline_peanuts.jpg/320px-Praline_peanuts.jpg", true, 12, 1.20),
        Producto(18, "KARI002", "Karinto", "Cinta Karinto con Pasas", "Tira/Cinta conteniendo 12 unidades de Mix de Maní y Pasas Karinto de 50g. Excelente balance de dulce y salado.", "Cinta de 12 un", 13.20, 40, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Trail_mix_with_peanuts_and_raisins.jpg/320px-Trail_mix_with_peanuts_and_raisins.jpg", true, 12, 1.30),
        Producto(19, "NATU001", "Natuchips", "Cinta Natuchips Plátano Sal", "Tira/Cinta conteniendo 12 unidades de Plátano Verde Frito con Sal Natuchips de 45g. Snacks naturales y crujientes.", "Cinta de 12 un", 15.00, 35, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Plantain_chips_plate.jpg/320px-Plantain_chips_plate.jpg", true, 12, 1.50),
        Producto(20, "NATU002", "Natuchips", "Cinta Natuchips Plátano Dulce", "Tira/Cinta conteniendo 12 unidades de Plátano Maduro Frito Natuchips de 45g. Toque dulce natural.", "Cinta de 12 un", 15.00, 30, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Sweet_plantain_chips.jpg/320px-Sweet_plantain_chips.jpg", true, 12, 1.50),
        Producto(21, "TORT001", "Tortees", "Cinta Tortees Picante", "Tira/Cinta conteniendo 12 unidades de Tortillas de Maíz Sabor Picante Tortees de 40g. Sabor mexicano picantito.", "Cinta de 12 un", 12.00, 45, "DISPONIBLE", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Tortilla_chips_with_chili.jpg/320px-Tortilla_chips_with_chili.jpg", true, 12, 1.20),
    )

    private val carritoItems = mutableMapOf<Int, CarritoItem>()
    private val pedidos = mutableListOf<Pedido>(
        Pedido(
            idPedido = 1001,
            idUsuario = 1,
            fechaPedido = "2026-07-08T12:00:00",
            estado = "Registrado",
            direccionEntrega = "Av. Principal 123",
            detalle = listOf(
                PedidoItem(
                    idProducto = 1,
                    nombreProducto = "Cinta Lays Clásicas",
                    cantidad = 2,
                    precioUnitario = 14.40,
                    subtotal = 28.80
                )
            ),
            total = 28.80
        )
    )
    private var pedidoCounter = 1001

    // ─── Autenticación ────────────────────────────────────────────────────────

    override suspend fun login(request: LoginRequest): ApiResponse<Usuario> {
        delay(500)
        return if (request.telefono == "987654321" && request.password == "FRITOLAY2026") {
            ApiResponse(true, "Login exitoso", mockUsuario)
        } else if (request.telefono == "999999999" && request.password == "admin123") {
            ApiResponse(true, "Login exitoso", mockAdmin)
        } else {
            ApiResponse(false, "Credenciales inválidas. Usa 987654321 / FRITOLAY2026 o administrador 999999999 / admin123", null)
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

    override suspend fun getTodosLosPedidos(): ApiResponse<List<Pedido>> {
        delay(300)
        return ApiResponse(true, null, pedidos.toList())
    }

    override suspend fun actualizarEstadoPedido(idPedido: Int, idEstado: Int): ApiResponse<Pedido> {
        delay(300)
        val estadoStr = when (idEstado) {
            1 -> "Registrado"
            2 -> "En preparación"
            3 -> "En camino"
            4 -> "Entregado"
            5 -> "Cancelado"
            else -> "Registrado"
        }
        val index = pedidos.indexOfFirst { it.idPedido == idPedido }
        if (index != -1) {
            val oldPedido = pedidos[index]
            val updated = oldPedido.copy(estado = estadoStr)
            pedidos[index] = updated
            return ApiResponse(true, "Estado actualizado", updated)
        }
        return ApiResponse(false, "Pedido no encontrado", null)
    }

    private fun buildCarrito(idUsuario: Int = 1) = Carrito(
        idCarrito = 1,
        idUsuario = idUsuario,
        items = carritoItems.values.toList(),
    )
}
