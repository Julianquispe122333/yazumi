package com.example.yazumi.ui.screens.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.yazumi.ui.components.CategoryGridCard
import com.example.yazumi.ui.components.ErrorMessage
import com.example.yazumi.ui.components.LoadingBox
import com.example.yazumi.ui.components.ProductCard
import com.example.yazumi.ui.viewmodel.CatalogViewModel

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Button

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onCategoryClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCategorias()
    }

    when {
        uiState.isLoading -> LoadingBox()
        uiState.error != null -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ErrorMessage(uiState.error ?: "Error al cargar el catálogo")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.loadCategorias() }) {
                    Text("Reintentar")
                }
            }
        }
        else -> Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Categorías de productos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.categorias) { categoria ->
                    CategoryGridCard(
                        categoria = categoria,
                        onClick = { onCategoryClick(categoria.nombre) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryProductsScreen(
    viewModel: com.example.yazumi.ui.viewmodel.CategoryProductsViewModel,
    onProductClick: (Int) -> Unit,
    onBack: () -> Unit,
    onNavigateToCart: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> LoadingBox()
        uiState.error != null -> ErrorMessage(uiState.error!!)
        else -> Column(modifier = Modifier.fillMaxSize()) {
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
                    text = uiState.marca,
                    style = MaterialTheme.typography.headlineSmall,
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.productos) { producto ->
                    ProductCard(
                        producto = producto,
                        onClick = { onProductClick(producto.idProducto) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
