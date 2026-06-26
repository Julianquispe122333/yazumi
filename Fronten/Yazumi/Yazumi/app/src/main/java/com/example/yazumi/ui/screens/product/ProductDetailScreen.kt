package com.example.yazumi.ui.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.yazumi.ui.components.ErrorMessage
import com.example.yazumi.ui.components.LoadingBox
import com.example.yazumi.ui.components.ProductImage
import com.example.yazumi.ui.components.QuantitySelector
import com.example.yazumi.ui.util.formatSoles
import com.example.yazumi.ui.viewmodel.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    var quantity by remember { mutableIntStateOf(1) }

    LaunchedEffect(uiState.message, uiState.error) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    when {
        uiState.isLoading -> LoadingBox()
        uiState.producto == null -> ErrorMessage(uiState.error ?: "Producto no encontrado")
        else -> {
            val producto = uiState.producto!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ProductImage(
                    imagenUrl = producto.imagen,
                    marca = producto.marca,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                )
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = producto.marca,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = formatSoles(producto.precio),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Presentación: ${producto.presentacion ?: "N/A"}")
                        Text("Stock disponible: ${producto.stock} unidades")
                        producto.descripcion?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(it, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                Text("Cantidad", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                QuantitySelector(
                    quantity = quantity,
                    onIncrease = { if (quantity < producto.stock) quantity++ },
                    onDecrease = { if (quantity > 1) quantity-- },
                )
                Button(
                    onClick = { viewModel.addToCart(quantity) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !uiState.isAdding && producto.stock > 0,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        if (uiState.isAdding) "Agregando..." else "Agregar al pedido",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}
