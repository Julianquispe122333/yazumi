package com.example.yazumi.data.repository

import com.example.yazumi.data.local.SessionManager
import com.example.yazumi.data.model.LoginRequest
import com.example.yazumi.data.model.RegisterRequest
import com.example.yazumi.data.model.Usuario
import com.example.yazumi.data.remote.YazumiApi
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val api: YazumiApi,
    private val sessionManager: SessionManager,
) {
    val isLoggedIn: Flow<Boolean> = sessionManager.isLoggedIn
    val currentUser: Flow<Usuario?> = sessionManager.currentUser

    suspend fun login(telefono: String, codigo: String): Result<Usuario> {
        val response = api.login(LoginRequest(telefono, codigo))
        return if (response.success && response.data != null) {
            sessionManager.saveUser(response.data)
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al iniciar sesión"))
        }
    }

    suspend fun register(
        nombres: String,
        telefono: String,
        direccion: String,
        nombreNegocio: String?,
        codigo: String,
    ): Result<Usuario> {
        val response = api.register(
            RegisterRequest(nombres, telefono, direccion, nombreNegocio, codigo),
        )
        return if (response.success && response.data != null) {
            sessionManager.saveUser(response.data)
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.message ?: "Error al registrarse"))
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}
