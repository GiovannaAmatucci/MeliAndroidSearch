package com.giovanna.amatucci.melisearch.data.remote

import com.giovanna.amatucci.melisearch.data.model.Product
import com.giovanna.amatucci.melisearch.data.model.ProductDetailResponse
import com.giovanna.amatucci.melisearch.data.model.SearchResponse
import com.giovanna.amatucci.melisearch.data.network.ApiResult

interface MeliApi {
    suspend fun searchProducts(query: String): ApiResult<SearchResponse>
    suspend fun getProductDetail(id: String): ApiResult<ProductDetailResponse>
}