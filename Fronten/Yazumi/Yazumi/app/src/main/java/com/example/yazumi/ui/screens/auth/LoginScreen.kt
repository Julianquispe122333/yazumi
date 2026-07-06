package com.example.yazumi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.yazumi.ui.theme.YazumiBlue
import com.example.yazumi.ui.theme.YazumiRed
import com.example.yazumi.ui.theme.YazumiYellow
import com.example.yazumi.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var telefono by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var showForgotPasswordDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) onLoginSuccess()
    }

    if (showForgotPasswordDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text("Recuperar contraseña", fontWeight = FontWeight.Bold) },
            text = {
                Text("Para restablecer su contraseña, comuníquese con el soporte o administrador de Yazumi indicando su número de teléfono celular registrado.")
            },
            confirmButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(YazumiYellow, YazumiRed.copy(alpha = 0.85f), YazumiBlue),
                ),
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Yazumi",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = "Pedidos Frito-Lay",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(20.dp),
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { showForgotPasswordDialog = true },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("¿Olvidaste tu contraseña?", style = MaterialTheme.typography.bodySmall)
                }
            }
            uiState.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Button(
                onClick = { viewModel.login(telefono.trim(), password.trim()) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !uiState.isLoading && telefono.isNotBlank() && password.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.height(24.dp))
                } else {
                    Text("Entrar")
                }
            }
            TextButton(onClick = onNavigateToRegister, modifier = Modifier.fillMaxWidth()) {
                Text("¿Nuevo cliente? Regístrate")
            }
        }
    }
}

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var codigoValidacion by rememberSaveable { mutableStateOf("") }
    var nombres by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var negocio by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.success) {
        if (uiState.success) onRegisterSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Registro de cliente",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        OutlinedTextField(
            value = codigoValidacion,
            onValueChange = { codigoValidacion = it },
            label = { Text("Código de validación") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it },
            label = { Text("Nombres") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = negocio,
            onValueChange = { negocio = it },
            label = { Text("Nombre del negocio (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        uiState.error?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = {
                viewModel.register(
                    codigoValidacion = codigoValidacion,
                    nombres = nombres,
                    telefono = telefono,
                    password = password,
                    direccion = direccion,
                    negocio = negocio,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && codigoValidacion.isNotBlank() && nombres.isNotBlank() &&
                telefono.isNotBlank() && password.isNotBlank() && direccion.isNotBlank(),
        ) {
            Text("Registrarme")
        }
        TextButton(onClick = onNavigateBack) { Text("Volver al login") }
    }
}
