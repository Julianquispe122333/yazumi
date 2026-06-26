package com.example.yazumi.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.yazumi.data.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "yazumi_session")

class SessionManager(private val context: Context) {

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_ID] != null
    }

    val currentUser: Flow<Usuario?> = context.dataStore.data.map { prefs ->
        val id = prefs[KEY_USER_ID] ?: return@map null
        Usuario(
            idUsuario = id,
            nombres = prefs[KEY_NOMBRES] ?: "",
            telefono = prefs[KEY_TELEFONO] ?: "",
            direccion = prefs[KEY_DIRECCION] ?: "",
            nombreNegocio = prefs[KEY_NEGOCIO],
        )
    }

    suspend fun saveUser(usuario: Usuario) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = usuario.idUsuario
            prefs[KEY_NOMBRES] = usuario.nombres
            prefs[KEY_TELEFONO] = usuario.telefono
            prefs[KEY_DIRECCION] = usuario.direccion
            usuario.nombreNegocio?.let { prefs[KEY_NEGOCIO] = it }
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    companion object {
        private val KEY_USER_ID = intPreferencesKey("user_id")
        private val KEY_NOMBRES = stringPreferencesKey("nombres")
        private val KEY_TELEFONO = stringPreferencesKey("telefono")
        private val KEY_DIRECCION = stringPreferencesKey("direccion")
        private val KEY_NEGOCIO = stringPreferencesKey("nombre_negocio")
    }
}
