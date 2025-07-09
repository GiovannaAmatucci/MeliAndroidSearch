package com.giovanna.amatucci.melisearch.domain.usecase

import androidx.paging.PagingData
import com.giovanna.amatucci.melisearch.domain.model.Products
import com.giovanna.amatucci.melisearch.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

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