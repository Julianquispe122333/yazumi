package com.example.yazumi.data.repository

import com.example.yazumi.data.local.SessionManager
import com.example.yazumi.data.model.CrearPedidoRequest
import com.example.yazumi.data.model.Pedido
import com.example.yazumi.data.remote.YazumiApi

class OrderRepository(
    private val api: YazumiApi,
    @Suppress("unused") private val sessionManager: SessionManager,
) {

    suspend fun crearPedido(direccion: String, observaciones: String?): Result<Pedido> {
        val response = api.crearPedido(CrearPedidoRequest(direccion, observaciones))
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al crear pedido"))
        }
    }

    suspend fun getPedidos(): Result<List<Pedido>> {
        val response = api.getPedidos()
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al cargar pedidos"))
        }
    }
}
