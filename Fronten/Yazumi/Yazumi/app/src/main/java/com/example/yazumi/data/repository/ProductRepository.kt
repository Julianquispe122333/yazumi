package com.example.yazumi.data.repository

import com.example.yazumi.data.model.Categoria
import com.example.yazumi.data.model.Producto
import com.example.yazumi.data.model.Promocion
import com.example.yazumi.data.remote.YazumiApi

class ProductRepository(private val api: YazumiApi) {

    suspend fun getProductos(marca: String? = null, busqueda: String? = null): Result<List<Producto>> {
        return try {
            val response = api.getProductos(marca, busqueda)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al cargar productos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProducto(id: Int): Result<Producto> {
        return try {
            val response = api.getProducto(id)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Producto no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategorias(): Result<List<Categoria>> {
        return try {
            val response = api.getCategorias()
            if (response.success && response.data != null) {
                Result.success(response.data.map { it.toCategoria() })
            } else {
                Result.failure(Exception(response.message ?: "Error al cargar categorías"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Favoritos: no hay endpoint real en el backend, se retorna
     * una selección de los primeros productos disponibles.
     */
    suspend fun getFavoritos(): Result<List<Producto>> {
        return getProductos().map { productos ->
            productos.filter { (it.stock ?: 0) > 0 }.take(4)
        }
    }

    /**
     * Promociones: no hay endpoint real aún. Se devuelven promos mock.
     */
    suspend fun getPromociones(): Result<List<Promocion>> {
        val promos = listOf(
            Promocion(1, "2x1 en Lays", "Lleva 2 bolsas de Lays al precio de 1", null, 50, 0xFFFFD100),
            Promocion(2, "Combo Doritos", "3 bolsas Doritos por S/ 10.00", null, 15, 0xFFE31837),
            Promocion(3, "Semana Cheetos", "10% de descuento en toda la línea Cheetos", null, 10, 0xFF003DA5),
        )
        return Result.success(promos)
    }
}

