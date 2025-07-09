package com.giovanna.amatucci.melisearch.presentation.features.home.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.giovanna.amatucci.melisearch.presentation.features.home.HomeViewModel
import com.giovanna.amatucci.melisearch.presentation.features.home.components.content.ProductDetailsContent
import com.giovanna.amatucci.melisearch.presentation.features.home.components.topbar.ProductDetailsTopBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    viewModel: HomeViewModel = koinViewModel(), onBackClick: () -> Unit, id: String
) {
    LaunchedEffect(key1 = id) { viewModel.getProductDetails(id) }
    val product by viewModel.products.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            ProductDetailsTopBar(
                title = product?.title ?: "", onBackClick = onBackClick
            )
        }, containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        ProductDetailsContent(
            product = product, modifier = Modifier.padding(paddingValues)
        )
    }
}


















