package com.example.yazumi.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.yazumi.data.model.CarritoItem
import com.example.yazumi.ui.components.ErrorMessage
import com.example.yazumi.ui.components.LoadingBox
import com.example.yazumi.ui.components.ProductImage
import com.example.yazumi.ui.components.QuantitySelector
import com.example.yazumi.ui.util.formatSoles
import com.example.yazumi.ui.viewmodel.CartViewModel

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onNavigateToCatalog: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    when {
        uiState.isLoading -> LoadingBox()
        uiState.orderConfirmed -> OrderConfirmation(
            pedidoId = uiState.confirmedCompra?.idPedido ?: 0,
            total = uiState.confirmedCompra?.total ?: 0.0,
            onDone = {
                viewModel.resetConfirmation()
                onNavigateToCatalog()
            },
        )
        uiState.error != null && uiState.carrito.items.isEmpty() -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ErrorMessage(uiState.error ?: "Error al conectar con el servidor")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.loadCart() }) {
                    Text("Reintentar")
                }
            }
        }
        else -> {
            val carrito = uiState.carrito
            if (carrito.items.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveShoppingCart,
                        contentDescription = "Pedido vacío",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.height(100.dp).width(100.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Tu pedido está vacío",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Agrega deliciosos snacks desde el catálogo para comenzar tu compra.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(carrito.items, key = { it.idProducto }) { item ->
                            CartItemRow(
                                item = item,
                                onQuantityChange = { viewModel.updateQuantity(item.idProducto, it) },
                                onRemoveClick = { viewModel.removeItem(item.idProducto) },
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text("Total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text(
                                    formatSoles(carrito.total),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = user?.direccion ?: "",
                                onValueChange = {},
                                label = { Text("Dirección de entrega") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                            )
                            uiState.error?.let {
                                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    viewModel.confirmOrder(user?.direccion ?: "")
                                },
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                enabled = !uiState.isConfirming,
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Text(
                                    if (uiState.isConfirming) "Confirmando..." else "Confirmar pedido",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CarritoItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProductImage(
                imagenUrl = item.imagen,
                marca = item.nombre.split(" ").firstOrNull() ?: "SN",
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
                    .padding(end = 12.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, fontWeight = FontWeight.SemiBold)
                Text(
                    "${formatSoles(item.precioUnitario)} c/u",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "Subtotal: ${formatSoles(item.subtotal)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                QuantitySelector(
                    quantity = item.cantidad,
                    onQuantityChange = { onQuantityChange(it) },
                )
            }
        }
    }
}

@Composable
private fun OrderConfirmation(pedidoId: Int, total: Double, onDone: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("✓", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.primary)
        Text("¡Pedido confirmado!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("N° de pedido: $pedidoId")
        Text("Total: ${formatSoles(total)}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onDone) { Text("Continuar comprando") }
    }
}
