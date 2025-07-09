package com.giovanna.amatucci.melisearch.domain.repository

import androidx.paging.PagingData
import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import com.giovanna.amatucci.melisearch.domain.model.ProductShorthand
import com.giovanna.amatucci.melisearch.domain.model.Products
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductDetails(productId: String): ResultWrapper<ProductDetail>
    suspend fun getProductsStream(query: String): Flow<PagingData<Products>>
    suspend fun saveSearchQuery(query: String)
    suspend fun getSearchQueries(): List<String>
}