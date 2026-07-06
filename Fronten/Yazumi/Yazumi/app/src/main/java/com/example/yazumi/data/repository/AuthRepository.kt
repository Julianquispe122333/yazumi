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

    /** Login con teléfono + contraseña (backend usa campo "password") */
    suspend fun login(telefono: String, password: String): Result<Usuario> {
        return try {
            val response = api.login(LoginRequest(telefono, password))
            if (response.success && response.data != null) {
                sessionManager.saveUser(response.data)
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al iniciar sesión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Registro: requiere código de validación + contraseña */
    suspend fun register(
        codigoValidacion: String,
        nombres: String,
        telefono: String,
        password: String,
        direccion: String,
        nombreNegocio: String?,
    ): Result<Usuario> {
        return try {
            val response = api.register(
                RegisterRequest(
                    codigoValidacion = codigoValidacion,
                    nombres = nombres,
                    telefono = telefono,
                    password = password,
                    direccion = direccion,
                    nombreNegocio = nombreNegocio,
                ),
            )
            if (response.success && response.data != null) {
                val usuario = response.data.toUsuario()
                sessionManager.saveUser(usuario)
                Result.success(usuario)
            } else {
                Result.failure(Exception(response.message ?: "Error al registrarse"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}
