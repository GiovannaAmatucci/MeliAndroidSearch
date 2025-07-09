package com.giovanna.amatucci.melisearch.domain.usecase


import com.giovanna.amatucci.melisearch.domain.model.ProductAttribute
import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import com.giovanna.amatucci.melisearch.domain.repository.ProductRepository
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetProductsDetailUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var productRepository: ProductRepository

    private lateinit var getProductsDetailUseCase: GetProductsDetailUseCase

    @Before
    fun setUp() {
        getProductsDetailUseCase = GetProductsDetailUseCaseImpl(productRepository)
    }

    @Test
    fun `invoke should return error when productId is invalid`() = runTest {
        val invalidProductId = "INVALID123"

        val result = getProductsDetailUseCase(invalidProductId)

        assertTrue(result is ResultWrapper.Error)
        assertEquals("Formato de ID inválido.", (result as ResultWrapper.Error).message)

        coVerify(exactly = 0) { productRepository.getProductDetails(any()) }
    }

    @Test
    fun `invoke should call repository and return success when productId is valid`() = runTest {
        val validProductId = "MLB123456"
        val productDetail = ProductDetail(
            id = validProductId,
            title = "Product Title",
            imageUrls = listOf("url1"),
            attributes = listOf(ProductAttribute("Attr", "Value")),
            description = "Product Description"
        )
        coEvery { productRepository.getProductDetails(validProductId) } returns ResultWrapper.Success(productDetail)

        val result = getProductsDetailUseCase(validProductId)

        coVerify(exactly = 1) { productRepository.getProductDetails(validProductId) }

        assertTrue(result is ResultWrapper.Success)
        assertEquals(productDetail, (result as ResultWrapper.Success).data)
    }

    @Test
    fun `invoke should call repository and return error when productId is valid but repository fails`() = runTest {
        val validProductId = "MLB654321"
        val errorMessage = "API Error"
        coEvery { productRepository.getProductDetails(validProductId) } returns ResultWrapper.Error(errorMessage)

        val result = getProductsDetailUseCase(validProductId)

        coVerify(exactly = 1) { productRepository.getProductDetails(validProductId) }

        assertTrue(result is ResultWrapper.Error)
        assertEquals(errorMessage, (result as ResultWrapper.Error).message)
    }
}