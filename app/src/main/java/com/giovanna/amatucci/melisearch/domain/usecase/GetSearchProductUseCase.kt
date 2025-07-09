package com.giovanna.amatucci.melisearch.domain.usecase

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.giovanna.amatucci.melisearch.data.db.SearchDao
import com.giovanna.amatucci.melisearch.domain.model.ProductShorthand
import com.giovanna.amatucci.melisearch.domain.model.Products
import com.giovanna.amatucci.melisearch.domain.repository.ProductRepository
import com.giovanna.amatucci.melisearch.domain.util.EmptySearchQueryException
import com.giovanna.amatucci.melisearch.domain.util.ShortSearchQueryException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GetSearchProductUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<Products>>
}

class GetSearchProductUseCaseImpl(
    private val searchRepository: ProductRepository
) : GetSearchProductUseCase {
    override suspend operator fun invoke(query: String): Flow<PagingData<Products>> {
        val trimmedQuery = query.trim()
        return searchRepository.getProductsStream(trimmedQuery)
    }
}