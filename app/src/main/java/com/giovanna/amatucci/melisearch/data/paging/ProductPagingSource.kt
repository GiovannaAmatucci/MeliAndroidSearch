package com.giovanna.amatucci.melisearch.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giovanna.amatucci.melisearch.data.api.MeliApi
import com.giovanna.amatucci.melisearch.data.mapper.toDomain
import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.domain.model.ProductShorthand
import com.giovanna.amatucci.melisearch.domain.model.Products

class ProductPagingSource(
    private val apiService: MeliApi,
    private val query: String
) : PagingSource<Int, Products>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Products> {
        return try {
            val offset = params.key ?: 0
            val response = apiService.searchProducts(query, offset, params.loadSize)
            when (response) {
                is ApiResult.Success -> {
                    if (response.data.results.isNotEmpty()) {
                        val domainData = response.data.results.map { it.toDomain() }
                        LoadResult.Page(
                            data = domainData,
                            prevKey = if (offset == 0) null else offset - params.loadSize,
                            nextKey = if (domainData.isEmpty()) null else offset + params.loadSize
                        )
                    } else {
                        LoadResult.Error(Throwable())
                    }
                }

                is ApiResult.Error -> LoadResult.Error(Throwable(response.message))
                is ApiResult.Exception -> LoadResult.Error(response.exception)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Products>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize)
                ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
    }
}