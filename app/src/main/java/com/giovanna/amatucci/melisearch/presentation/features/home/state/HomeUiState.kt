package com.giovanna.amatucci.melisearch.presentation.features.home.state

import androidx.paging.PagingData
import com.giovanna.amatucci.melisearch.domain.model.Products
import kotlinx.coroutines.flow.StateFlow

sealed interface HomeUiState {
    object Idle : HomeUiState
    object Loading : HomeUiState
    data class Success(val products: StateFlow<PagingData<Products>>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}