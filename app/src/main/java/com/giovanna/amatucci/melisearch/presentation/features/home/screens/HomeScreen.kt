package com.giovanna.amatucci.melisearch.presentation.features.home.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.melisearch.presentation.features.home.components.content.HomeProductsContent
import com.giovanna.amatucci.melisearch.presentation.features.home.HomeViewModel
import com.giovanna.amatucci.melisearch.presentation.features.home.components.textstate.HomeErrorState
import com.giovanna.amatucci.melisearch.presentation.features.home.components.image.HomeInitialState
import com.giovanna.amatucci.melisearch.presentation.features.home.components.content.HomeLoadingState
import com.giovanna.amatucci.melisearch.presentation.features.home.components.topbar.HomeTopBar
import com.giovanna.amatucci.melisearch.presentation.features.home.state.HomeUiState
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToDetail: (id: String) -> Unit
) {
    val searchState by viewModel.searchQuery.collectAsStateWithLifecycle()
    val state by viewModel.searchUiState.collectAsStateWithLifecycle()
    val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getSearchHistory()
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                searchQuery = searchState,
                searchHistory = searchHistory,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onSearch = { viewModel.searchProducts(it) },
                onHistoryItemClicked = { viewModel.onHistoryItemClicked(it) }
            )
        }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .padding(paddingValues)
        ) {
            when (val uiState = state) {
                is HomeUiState.Success -> {
                    val products = uiState.products.collectAsLazyPagingItems()
                    HomeProductsContent(
                        products = products,
                        onProductClick = { navigateToDetail(it.id) }
                    )
                }

                is HomeUiState.Loading -> HomeLoadingState()
                is HomeUiState.Error -> HomeErrorState()
                else -> HomeInitialState()
            }
        }
    }
}












