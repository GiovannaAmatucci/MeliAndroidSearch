package com.giovanna.amatucci.melisearch.data.api

import com.giovanna.amatucci.melisearch.data.model.ProductDetailResponse
import com.giovanna.amatucci.melisearch.data.model.SearchResponse
import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.data.network.NetworkHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess

class MeliApiImpl(private val client: NetworkHttpClient) : MeliApi {
    override suspend fun searchProducts(
        query: String,
        offset: Int,
        limit: Int
    ): ApiResult<SearchResponse> {
        return try {
            val response = client().get("products/search") {
                parameter("status", "active")
                parameter("site_id", "MLB")
                parameter("q", query)
                parameter("offset", offset)
                parameter("limit", limit)
            }
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.description)
            }
        } catch (e: Exception) { ApiResult.Exception(e) }
    }

    override suspend fun getProductDetail(id: String): ApiResult<ProductDetailResponse> {
        return try {
            val response = client().get("products/$id")
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.description)
            }
        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}
