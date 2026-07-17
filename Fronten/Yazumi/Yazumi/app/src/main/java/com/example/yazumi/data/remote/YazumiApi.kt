package com.example.yazumi.data.remote

import com.example.yazumi.data.model.ActualizarCarritoRequest
import com.example.yazumi.data.model.AgregarCarritoRequest
import com.example.yazumi.data.model.ApiResponse
import com.example.yazumi.data.model.Carrito
import com.example.yazumi.data.model.CategoriaResponseCompat
import com.example.yazumi.data.model.CompraResponse
import com.example.yazumi.data.model.ComprarRequest
import com.example.yazumi.data.model.LoginRequest
import com.example.yazumi.data.model.Pedido
import com.example.yazumi.data.model.Producto
import com.example.yazumi.data.model.RegisterRequest
import com.example.yazumi.data.model.RegistroResponse
import com.example.yazumi.data.model.Usuario
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface YazumiApi {

    // ─── Autenticación ────────────────────────────────────────────────────────

    /** POST /api/auth/login → {telefono, password} → LoginResponseDTO */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<Usuario>

    /** POST /api/usuarios/registrar → RegistroRequestDTO → RegistroResponseDTO */
    @POST("usuarios/registrar")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegistroResponse>

    // ─── Productos ────────────────────────────────────────────────────────────

    /** GET /api/productos?marca=X&busqueda=Y */
    @GET("productos")
    suspend fun getProductos(
        @Query("marca") marca: String? = null,
        @Query("busqueda") busqueda: String? = null,
    ): ApiResponse<List<Producto>>

    /** GET /api/productos/{id} */
    @GET("productos/{id}")
    suspend fun getProducto(@Path("id") id: Int): ApiResponse<Producto>

    /** GET /api/productos/categorias */
    @GET("productos/categorias")
    suspend fun getCategorias(): ApiResponse<List<CategoriaResponseCompat>>

    // ─── Carrito ──────────────────────────────────────────────────────────────

    /** GET /api/carrito/{idUsuario} */
    @GET("carrito/{idUsuario}")
    suspend fun getCarrito(@Path("idUsuario") idUsuario: Int): ApiResponse<Carrito>

    /** POST /api/carrito/agregar → {idUsuario, idProducto, cantidad} */
    @POST("carrito/agregar")
    suspend fun agregarAlCarrito(@Body request: AgregarCarritoRequest): ApiResponse<Carrito>

    /** PUT /api/carrito/actualizar → {idUsuario, idProducto, cantidad} */
    @PUT("carrito/actualizar")
    suspend fun actualizarCarritoItem(@Body request: ActualizarCarritoRequest): ApiResponse<Carrito>

    /** DELETE /api/carrito/{idUsuario}/{idProducto} */
    @DELETE("carrito/{idUsuario}/{idProducto}")
    suspend fun eliminarDelCarrito(
        @Path("idUsuario") idUsuario: Int,
        @Path("idProducto") idProducto: Int,
    ): ApiResponse<Carrito>

    /** DELETE /api/carrito/vaciar/{idUsuario} */
    @DELETE("carrito/vaciar/{idUsuario}")
    suspend fun vaciarCarrito(@Path("idUsuario") idUsuario: Int): ApiResponse<Carrito>

    /** POST /api/carrito/comprar/{idUsuario} */
    @POST("carrito/comprar/{idUsuario}")
    suspend fun comprar(
        @Path("idUsuario") idUsuario: Int,
        @Body request: ComprarRequest,
    ): ApiResponse<CompraResponse>

    // ─── Pedidos ──────────────────────────────────────────────────────────────

    /** GET /api/pedidos/usuario/{idUsuario} */
    @GET("pedidos/usuario/{idUsuario}")
    suspend fun getPedidosPorUsuario(@Path("idUsuario") idUsuario: Int): ApiResponse<List<Pedido>>

    /** GET /api/pedidos/{idPedido} */
    @GET("pedidos/{idPedido}")
    suspend fun getPedido(@Path("idPedido") idPedido: Int): ApiResponse<Pedido>

    /** GET /api/pedidos (Administrador - todos los pedidos) */
    @GET("pedidos")
    suspend fun getTodosLosPedidos(): ApiResponse<List<Pedido>>

    /** PUT /api/pedidos/{idPedido}/estado/{idEstado} (Administrador - cambiar estado) */
    @PUT("pedidos/{idPedido}/estado/{idEstado}")
    suspend fun actualizarEstadoPedido(
        @Path("idPedido") idPedido: Int,
        @Path("idEstado") idEstado: Int
    ): ApiResponse<Pedido>
}

