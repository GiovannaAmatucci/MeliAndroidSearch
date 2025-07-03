package com.giovanna.amatucci.melisearch.data.remote

import com.giovanna.amatucci.melisearch.data.model.Product
import com.giovanna.amatucci.melisearch.data.model.ProductDetailResponse
import com.giovanna.amatucci.melisearch.data.model.SearchResponse
import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.data.network.NetworkHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

class MeliApiImpl(private val client: NetworkHttpClient) : MeliApi {
    override suspend fun searchProducts(query: String): ApiResult<SearchResponse> {
        val response = client.invoke().get("sites/MLB/search?q=$query")
        return try {
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.description)
            }
        } catch (e: Exception) {
            ApiResult.Exception(e)
        } finally {
            client.invoke().close()
        }
    }

    override suspend fun getProductDetail(id: String): ApiResult<ProductDetailResponse> {
        val response = client.invoke().get("items/$id")
        return try {
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(response.status.description)
            }
        } catch (e: Exception) {
            ApiResult.Exception(e)
        } finally {
            client.invoke().close()
        }
    }
}