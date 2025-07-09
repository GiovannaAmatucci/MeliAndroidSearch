package com.giovanna.amatucci.melisearch.presentation.features.home.components.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.giovanna.amatucci.melisearch.domain.model.Products
import com.giovanna.amatucci.melisearch.presentation.features.home.components.textstate.HomeEmptyState
import com.giovanna.amatucci.melisearch.presentation.features.home.components.textstate.HomeErrorState
import com.giovanna.amatucci.melisearch.presentation.features.home.components.image.ProductGrid

@Composable
fun HomeProductsContent(
    products: LazyPagingItems<Products>, onProductClick: (Products) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        ProductGrid(
            products = products, onProductClick = onProductClick
        )
        products.loadState.apply {
            when {
                refresh is LoadState.Loading -> HomeLoadingState()
                refresh is LoadState.Error -> HomeErrorState()
                refresh is LoadState.NotLoading && products.itemCount == 0 -> {
                    HomeEmptyState()
                }
            }
        }
    }
}