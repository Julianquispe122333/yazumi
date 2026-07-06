package com.example.yazumi.data.repository

import com.example.yazumi.data.local.SessionManager
import com.example.yazumi.data.model.Pedido
import com.example.yazumi.data.remote.YazumiApi
import kotlinx.coroutines.flow.firstOrNull

class OrderRepository(
    private val api: YazumiApi,
    private val sessionManager: SessionManager,
) {

    private suspend fun currentUserId(): Int =
        sessionManager.currentUser.firstOrNull()?.idUsuario ?: 0

    /** Obtiene el historial de pedidos del usuario actual */
    suspend fun getPedidos(): Result<List<Pedido>> {
        return try {
            val uid = currentUserId()
            if (uid == 0) {
                return Result.success(emptyList())
            }
            val response = api.getPedidosPorUsuario(uid)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al cargar pedidos"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.parseErrorMessage()))
        }
    }

    /** Obtiene el detalle de un pedido específico */
    suspend fun getPedido(idPedido: Int): Result<Pedido> {
        return try {
            val response = api.getPedido(idPedido)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al obtener el pedido"))
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

