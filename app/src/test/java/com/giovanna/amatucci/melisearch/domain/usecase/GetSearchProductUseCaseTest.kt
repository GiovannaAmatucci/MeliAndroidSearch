package com.giovanna.amatucci.melisearch.domain.usecase

import app.cash.turbine.test
import androidx.paging.PagingData
import com.giovanna.amatucci.melisearch.domain.model.Products
import com.giovanna.amatucci.melisearch.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetSearchProductUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var searchRepository: ProductRepository

    private lateinit var getSearchProductUseCase: GetSearchProductUseCase

    @Before
    fun setUp() {
        getSearchProductUseCase = GetSearchProductUseCaseImpl(searchRepository)
    }

    @Test
    fun `invoke should trim query and call repository`() = runTest {
        val queryWithSpaces = "  iphone 15  "
        val trimmedQuery = "iphone 15"
        val expectedPagingData = PagingData.from(listOf<Products>())

        coEvery { searchRepository.getProductsStream(trimmedQuery) } returns flowOf(expectedPagingData)

        val resultFlow = getSearchProductUseCase(queryWithSpaces)
        val resultData = resultFlow.first()

        coVerify(exactly = 1) { searchRepository.getProductsStream(trimmedQuery) }

        assertEquals(expectedPagingData, resultData)
    }

    @Test
    fun `invoke should return the flow from repository`() = runTest {
        val query = "samsung"
        val product = Products(
            id = "MLB123",
            name = "Samsung S23",
            imageUrls = emptyList(),
            brand = null, model = null, color = null, internalMemory = null, ram = null,
            releaseYear = null, screenSize = null, screenResolution = null,
            mainRearCamera = null, mainFrontCamera = null, description = null
        )
        val expectedPagingData = PagingData.from(listOf(product))

        coEvery { searchRepository.getProductsStream(query) } returns flowOf(expectedPagingData)

        val resultFlow = getSearchProductUseCase(query)

        resultFlow.test {
            assertEquals(expectedPagingData, awaitItem())
            awaitComplete()
        }
    }
}