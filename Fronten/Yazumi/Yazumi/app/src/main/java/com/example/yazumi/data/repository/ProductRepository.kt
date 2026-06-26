package com.example.yazumi.data.repository

import com.example.yazumi.data.model.Categoria
import com.example.yazumi.data.model.Producto
import com.example.yazumi.data.model.Promocion
import com.example.yazumi.data.remote.YazumiApi

class ProductRepository(private val api: YazumiApi) {

    suspend fun getProductos(marca: String? = null, busqueda: String? = null): Result<List<Producto>> {
        val response = api.getProductos(marca, busqueda)
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al cargar productos"))
        }
    }

    suspend fun getProducto(id: Int): Result<Producto> {
        val response = api.getProducto(id)
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Producto no encontrado"))
        }
    }

    suspend fun getCategorias(): Result<List<Categoria>> {
        val response = api.getCategorias()
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al cargar categorías"))
        }
    }

    suspend fun getFavoritos(): Result<List<Producto>> {
        val response = api.getFavoritos()
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al cargar favoritos"))
        }
    }

    suspend fun getPromociones(): Result<List<Promocion>> {
        val response = api.getPromociones()
        return if (response.success && response.data != null) {
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al cargar promociones"))
        }
    }
}
