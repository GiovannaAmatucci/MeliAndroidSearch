package com.giovanna.amatucci.melisearch.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import com.giovanna.amatucci.melisearch.domain.model.Products
import com.giovanna.amatucci.melisearch.domain.repository.ProductRepository
import com.giovanna.amatucci.melisearch.domain.usecase.GetProductsDetailUseCase
import com.giovanna.amatucci.melisearch.domain.usecase.GetSearchProductUseCase
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import com.giovanna.amatucci.melisearch.presentation.features.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: GetSearchProductUseCase,
    private val productDetailUseCase: GetProductsDetailUseCase,
    private val repository: ProductRepository
) : ViewModel() {
    private val _searchUiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val searchUiState: StateFlow<HomeUiState> get() = _searchUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery.asStateFlow()

    private val _products = MutableStateFlow<ProductDetail?>(null)
    val products: StateFlow<ProductDetail?> get() = _products.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory.asStateFlow()

    private val _productsFlow = MutableStateFlow<PagingData<Products>>(PagingData.empty())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            repository.saveSearchQuery(query)
            _searchUiState.value = HomeUiState.Loading
            useCase.invoke(query).catch {
                _searchUiState.value = HomeUiState.Error(it.localizedMessage ?: "Erro desconhecido")
                _productsFlow.value = PagingData.empty()
            }.cachedIn(viewModelScope).collect { pagingData ->
                _productsFlow.value = pagingData
                _searchUiState.value = HomeUiState.Success(_productsFlow)
                getSearchHistory()
            }
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            val history = repository.getSearchQueries()
            history.takeIf { it.isNotEmpty() }?.let { _searchHistory.value = it }
        }
    }

    fun onHistoryItemClicked(query: String) {
        onSearchQueryChanged(query)
        searchProducts(query)
    }

    fun getProductDetails(productId: String) {
        viewModelScope.launch {
            val response = productDetailUseCase.invoke(productId)
            when (response) {
                is ResultWrapper.Success -> {
                    response.data?.let {
                        _products.value = it
                    }
                }

                is ResultWrapper.Error -> {
                    _searchUiState.value = HomeUiState.Error(response.message.orEmpty())
                }

                else -> {
                    _searchUiState.value = HomeUiState.Loading
                }
            }
        }
    }
}
