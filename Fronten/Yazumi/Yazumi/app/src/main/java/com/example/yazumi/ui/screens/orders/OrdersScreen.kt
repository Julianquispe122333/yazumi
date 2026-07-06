package com.example.yazumi.ui.screens.orders

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.yazumi.data.model.Pedido
import com.example.yazumi.ui.components.ErrorMessage
import com.example.yazumi.ui.components.LoadingBox
import com.example.yazumi.ui.util.formatSoles
import com.example.yazumi.ui.viewmodel.OrderHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(viewModel: OrderHistoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Recargar pedidos automaticamente cuando la pantalla se vuelve visible
    LaunchedEffect(Unit) {
        viewModel.loadPedidos()
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.loadPedidos() },
        modifier = Modifier.fillMaxSize(),
    ) {
        when {
            uiState.isLoading && uiState.pedidos.isEmpty() -> LoadingBox()
            uiState.error != null && uiState.pedidos.isEmpty() -> ErrorMessage(uiState.error!!)
            uiState.pedidos.isEmpty() -> EmptyOrders()
            else -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(
                        "Mis pedidos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
                items(uiState.pedidos, key = { it.idPedido }) { pedido ->
                    PedidoCard(pedido = pedido)
                }
            }
        }
    }
}

@Composable
private fun EmptyOrders() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            modifier = Modifier.height(100.dp).width(100.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "No tienes pedidos aún",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Agrega deliciosos snacks desde el catálogo y realiza tu primer pedido.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PedidoCard(pedido: Pedido) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado: N° pedido + estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Pedido #${pedido.idPedido}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                EstadoChip(estado = pedido.estado)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                pedido.fechaPedido,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                pedido.direccionEntrega,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (pedido.items.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                pedido.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            "${item.cantidad}x ${item.nombreProducto}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            formatSoles(item.subtotal),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Total", fontWeight = FontWeight.Bold)
                Text(
                    formatSoles(pedido.total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun EstadoChip(estado: String) {
    val (icon, color) = estadoIconAndColor(estado)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = estado,
            tint = color,
            modifier = Modifier.height(16.dp),
        )
        Text(
            text = estado,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = color,
        )
    }
}

private fun estadoIconAndColor(estado: String): Pair<ImageVector, Color> = when (estado.lowercase()) {
    "registrado" -> Pair(Icons.Default.HourglassTop, Color(0xFFF59E0B))
    "en preparación", "en preparacion" -> Pair(Icons.Default.HourglassTop, Color(0xFF3B82F6))
    "en camino" -> Pair(Icons.Default.LocalShipping, Color(0xFF8B5CF6))
    "entregado" -> Pair(Icons.Default.CheckCircle, Color(0xFF10B981))
    "cancelado" -> Pair(Icons.Default.DeliveryDining, Color(0xFFEF4444))
    else -> Pair(Icons.Default.Receipt, Color(0xFF6B7280))
}
