package com.giovanna.amatucci.melisearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.giovanna.amatucci.melisearch.data.api.MeliApi
import com.giovanna.amatucci.melisearch.data.db.SearchDao
import com.giovanna.amatucci.melisearch.data.mapper.toDomain
import com.giovanna.amatucci.melisearch.data.model.SearchEntity
import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.data.paging.ProductPagingSource
import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import com.giovanna.amatucci.melisearch.domain.repository.ProductRepository
import com.giovanna.amatucci.melisearch.di.CoroutineDispatchers
import com.giovanna.amatucci.melisearch.domain.model.Products
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val meliApi: MeliApi,
    private val dao: SearchDao,
    private val ioDispatchers: CoroutineDispatchers
) : ProductRepository {
    override suspend fun getProductDetails(productId: String): ResultWrapper<ProductDetail> {
        return withContext(ioDispatchers.io()) {
            val detailResult = meliApi.getProductDetail(productId)

            if (detailResult is ApiResult.Success) {
                ResultWrapper.Success(detailResult.data.toDomain())
            } else {
                val firstError = findFirstError(detailResult)
                ResultWrapper.Error(firstError)
            }
        }
    }

    override suspend fun getProductsStream(query: String): Flow<PagingData<Products>> {
        saveSearchQuery(query)
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { ProductPagingSource(meliApi, query) }
        ).flow
    }

    override suspend fun saveSearchQuery(query: String) {
        withContext(ioDispatchers.default()) {
            if (query.isBlank()) { return@withContext }
            val currentHistory = dao.getSearchHistory()
            val oldQueries = currentHistory?.queries?.toMutableList() ?: mutableListOf()
            oldQueries.remove(query)
            oldQueries.add(0, query)
            val newQueries = oldQueries.take(10)
            val newHistory = SearchEntity(id = currentHistory?.id ?: 0, queries = newQueries)
            dao.insertSearch(newHistory)
        }
    }

    override suspend fun getSearchQueries(): List<String> {
        val result = withContext(ioDispatchers.default()) {
            val currentHistory = dao.getSearchHistory()
            if (currentHistory?.queries.isNullOrEmpty()) {
                return@withContext emptyList()
            }
            return@withContext currentHistory.queries
        }
        return result
    }

    private fun findFirstError(vararg results: ApiResult<*>): String {
        for (result in results) {
            return when (result) {
                is ApiResult.Error -> result.message
                is ApiResult.Exception -> result.exception.localizedMessage.orEmpty()
                is ApiResult.Success -> continue
            }
        }
        return String()
    }
}

