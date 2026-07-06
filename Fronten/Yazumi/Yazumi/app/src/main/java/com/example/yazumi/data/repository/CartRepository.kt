package com.example.yazumi.data.repository

import com.example.yazumi.data.local.SessionManager
import com.example.yazumi.data.model.AgregarCarritoRequest
import com.example.yazumi.data.model.ActualizarCarritoRequest
import com.example.yazumi.data.model.Carrito
import com.example.yazumi.data.model.CarritoItem
import com.example.yazumi.data.model.CompraResponse
import com.example.yazumi.data.model.ComprarRequest
import com.example.yazumi.data.remote.YazumiApi
import kotlinx.coroutines.flow.firstOrNull

class CartRepository(
    private val api: YazumiApi,
    private val sessionManager: SessionManager,
) {

    private suspend fun currentUserId(): Int =
        sessionManager.currentUser.firstOrNull()?.idUsuario ?: 0

    suspend fun getCarrito(): Result<Carrito> {
        return try {
            val uid = currentUserId()
            if (uid == 0) {
                return Result.success(Carrito(0, 0, emptyList()))
            }
            val response = api.getCarrito(uid)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al cargar carrito"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.parseErrorMessage()))
        }
    }

    /** Agrega un producto al carrito (crea el item si no existe) */
    suspend fun agregarItem(idProducto: Int, cantidad: Int): Result<Carrito> {
        return try {
            val uid = currentUserId()
            if (uid == 0) {
                return Result.failure(Exception("Usuario no autenticado"))
            }
            val response = api.agregarAlCarrito(AgregarCarritoRequest(uid, idProducto, cantidad))
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al agregar producto"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.parseErrorMessage()))
        }
    }

    /** Actualiza la cantidad de un item existente en el carrito */
    suspend fun updateItem(idProducto: Int, cantidad: Int): Result<Carrito> {
        return try {
            val uid = currentUserId()
            if (uid == 0) {
                return Result.failure(Exception("Usuario no autenticado"))
            }
            if (cantidad <= 0) {
                // Si cantidad es 0 o menor, eliminar el item
                eliminarItem(idProducto)
            } else {
                val response = api.actualizarCarritoItem(ActualizarCarritoRequest(uid, idProducto, cantidad))
                if (response.success && response.data != null) {
                    Result.success(response.data)
                } else {
                    Result.failure(Exception(response.message ?: "Error al actualizar carrito"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.parseErrorMessage()))
        }
    }

    /** Elimina un producto del carrito */
    suspend fun eliminarItem(idProducto: Int): Result<Carrito> {
        return try {
            val uid = currentUserId()
            if (uid == 0) {
                return Result.failure(Exception("Usuario no autenticado"))
            }
            val response = api.eliminarDelCarrito(uid, idProducto)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al eliminar producto"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.parseErrorMessage()))
        }
    }

    /** Vacía completamente el carrito */
    suspend fun vaciarCarrito(): Result<Carrito> {
        return try {
            val uid = currentUserId()
            if (uid == 0) {
                return Result.failure(Exception("Usuario no autenticado"))
            }
            val response = api.vaciarCarrito(uid)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al vaciar carrito"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.parseErrorMessage()))
        }
    }

    /** Confirma la compra del carrito y crea un pedido */
    suspend fun comprar(direccionEntrega: String): Result<CompraResponse> {
        return try {
            val uid = currentUserId()
            if (uid == 0) {
                return Result.failure(Exception("Usuario no autenticado"))
            }
            val response = api.comprar(uid, ComprarRequest(direccionEntrega))
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al realizar la compra"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.parseErrorMessage()))
        }
    }

    private fun Throwable.parseErrorMessage(): String {
        if (this is retrofit2.HttpException) {
            try {
                val errorBody = this.response()?.errorBody()?.string()
                if (!errorBody.isNullOrBlank()) {
                    val apiResponse = com.google.gson.Gson().fromJson(errorBody, com.example.yazumi.data.model.ApiResponse::class.java)
                    return apiResponse.message ?: "Error del servidor"
                }
            } catch (ex: Exception) {
                // ignorar
            }
        }
        return this.message ?: "Error de red"
    }
}

