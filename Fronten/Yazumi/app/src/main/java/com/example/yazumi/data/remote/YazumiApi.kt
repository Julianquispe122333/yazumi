package com.example.yazumi.data.remote

import com.example.yazumi.data.model.ApiResponse
import com.example.yazumi.data.model.Carrito
import com.example.yazumi.data.model.Categoria
import com.example.yazumi.data.model.CrearPedidoRequest
import com.example.yazumi.data.model.LoginRequest
import com.example.yazumi.data.model.Pedido
import com.example.yazumi.data.model.Producto
import com.example.yazumi.data.model.Promocion
import com.example.yazumi.data.model.RegisterRequest
import com.example.yazumi.data.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface YazumiApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<Usuario>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<Usuario>

    @GET("productos")
    suspend fun getProductos(
        @Query("marca") marca: String? = null,
        @Query("busqueda") busqueda: String? = null,
    ): ApiResponse<List<Producto>>

    @GET("productos/{id}")
    suspend fun getProducto(@Path("id") id: Int): ApiResponse<Producto>

    @GET("productos/categorias")
    suspend fun getCategorias(): ApiResponse<List<Categoria>>

    @GET("productos/favoritos")
    suspend fun getFavoritos(): ApiResponse<List<Producto>>

    @GET("promociones")
    suspend fun getPromociones(): ApiResponse<List<Promocion>>

    @GET("carrito")
    suspend fun getCarrito(): ApiResponse<Carrito>

    @PUT("carrito/items/{productoId}")
    suspend fun updateCarritoItem(
        @Path("productoId") productoId: Int,
        @Query("cantidad") cantidad: Int,
    ): ApiResponse<Carrito>

    @POST("pedidos")
    suspend fun crearPedido(@Body request: CrearPedidoRequest): ApiResponse<Pedido>

    @GET("pedidos")
    suspend fun getPedidos(): ApiResponse<List<Pedido>>
}
