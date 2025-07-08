package com.giovanna.amatucci.melisearch.data.api

import com.giovanna.amatucci.melisearch.data.model.ProductDetailResponse
import com.giovanna.amatucci.melisearch.data.model.SearchResponse
import com.giovanna.amatucci.melisearch.data.network.ApiResult

interface MeliApi {
    suspend fun searchProducts(
        query: String,
        offset: Int,
        limit: Int
    ): ApiResult<SearchResponse>

    suspend fun getProductDetail(id: String): ApiResult<ProductDetailResponse>
}