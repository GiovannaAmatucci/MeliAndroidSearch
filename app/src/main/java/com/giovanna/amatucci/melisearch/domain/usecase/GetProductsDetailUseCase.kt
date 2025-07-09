package com.giovanna.amatucci.melisearch.domain.usecase

import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import com.giovanna.amatucci.melisearch.domain.repository.ProductRepository
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper

interface GetProductsDetailUseCase {
    suspend operator fun invoke(productId: String): ResultWrapper<ProductDetail>
}

class GetProductsDetailUseCaseImpl(
    private val productRepository: ProductRepository,
) : GetProductsDetailUseCase {
    override suspend fun invoke(productId: String): ResultWrapper<ProductDetail> {
        val productIdRegex = Regex("^MLB\\d+$")
        if (!productId.matches(productIdRegex)) {
            return ResultWrapper.Error("Formato de ID inválido.")
        }
        return productRepository.getProductDetails(productId)
    }
}
