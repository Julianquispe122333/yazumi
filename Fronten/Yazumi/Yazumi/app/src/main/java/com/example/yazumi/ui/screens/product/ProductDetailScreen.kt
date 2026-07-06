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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
    onBack: () -> Unit,
    onNavigateToCart: () -> Unit,
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "Detalle de Producto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                    )
                    IconButton(onClick = onNavigateToCart) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ver pedido",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProductImage(
                        imagenUrl = producto.imagen,
                        marca = producto.marca ?: "",
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
                    text = producto.marca ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = formatSoles(producto.precio),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                val costoUnitario = producto.precio / (if (producto.unidadesPorPaquete > 0) producto.unidadesPorPaquete else 1)
                val gananciaUnidad = producto.precioSugerido - costoUnitario
                val gananciaPaquete = gananciaUnidad * producto.unidadesPorPaquete

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "INFORMACIÓN MAYORISTA",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        androidx.compose.material3.HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        Text(
                            text = "Contenido: ${producto.presentacion ?: "N/A"} (${producto.unidadesPorPaquete} un.)",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(text = "Costo unitario mayorista: ${formatSoles(costoUnitario)}")
                        Text(text = "Precio venta público sugerido: ${formatSoles(producto.precioSugerido)}")
                        Text(
                            text = "Ganancia estimada: ${formatSoles(gananciaPaquete)} por empaque (${formatSoles(gananciaUnidad)} por unidad)",
                            color = Color(0xFF10B981), // Verde éxito
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Stock disponible: ${producto.stock} empaques")
                        producto.descripcion?.let {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(it, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                Text("Cantidad (Empaques)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                QuantitySelector(
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
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
}
