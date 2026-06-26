package com.example.yazumi.data.repository

import com.example.yazumi.data.local.SessionManager
import com.example.yazumi.data.model.Carrito
import com.example.yazumi.data.remote.YazumiApi

class CartRepository(
    private val api: YazumiApi,
    @Suppress("unused") private val sessionManager: SessionManager,
) {

    suspend fun getCarrito(): Result<Carrito> {
        val response = api.getCarrito()
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al cargar carrito"))
        }
    }

    suspend fun updateItem(productoId: Int, cantidad: Int): Result<Carrito> {
        val response = api.updateCarritoItem(productoId, cantidad)
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al actualizar carrito"))
        }
    }
}
