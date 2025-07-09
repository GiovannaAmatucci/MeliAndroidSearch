package com.giovanna.amatucci.melisearch.presentation.features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.giovanna.amatucci.melisearch.domain.model.ProductAttribute
import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import com.giovanna.amatucci.melisearch.domain.model.Products
import com.giovanna.amatucci.melisearch.domain.repository.ProductRepository
import com.giovanna.amatucci.melisearch.domain.usecase.GetProductsDetailUseCase
import com.giovanna.amatucci.melisearch.domain.usecase.GetSearchProductUseCase
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import com.giovanna.amatucci.melisearch.presentation.features.home.state.HomeUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var getSearchProductUseCase: GetSearchProductUseCase

    @RelaxedMockK
    private lateinit var productRepository: ProductRepository

    @RelaxedMockK
    private lateinit var getProductsDetailUseCase: GetProductsDetailUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(
            getSearchProductUseCase, getProductsDetailUseCase,
            repository = productRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchQueryChanged should update searchQuery state`() = runTest {
        val query = "test query"
        viewModel.onSearchQueryChanged(query)
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `searchProducts should update state to Success when use case returns data`() = runTest {
        val query = "iphone"
        val product = Products(
            id = "MLB12345",
            name = "Apple iPhone 13",
            imageUrls = listOf("http://http2.mlstatic.com/D_938294-MLA48053264197_102021-I.jpg"),
            brand = "Apple",
            model = "iPhone 13",
            color = "Preto",
            internalMemory = "128 GB",
            ram = "4 GB",
            releaseYear = "2021",
            screenSize = "6.1\"",
            screenResolution = "1170 px x 2532 px",
            mainRearCamera = "12 Mpx",
            mainFrontCamera = "12 Mpx",
            description = "O mais novo celular da Apple."
        )
        val pagingData = PagingData.from(listOf(product))
        val pagingDataFlow = flowOf(pagingData)

        coEvery { getSearchProductUseCase(query) } returns pagingDataFlow

        viewModel.searchUiState.test {
            assertEquals(HomeUiState.Idle, awaitItem())

            viewModel.searchProducts(query)

            assertEquals(HomeUiState.Loading, awaitItem())

            val successState = awaitItem()
            assertIs<HomeUiState.Success>(successState)

            coVerify { productRepository.saveSearchQuery(query) }
            coVerify { getSearchProductUseCase(query) }
            coVerify { productRepository.getSearchQueries() }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `searchProducts should update state to Error when use case throws exception`() = runTest {
        val query = "error"
        val errorMessage = "Something went wrong"
        coEvery { getSearchProductUseCase(query) } returns flow { throw RuntimeException(errorMessage) }

        viewModel.searchUiState.test {
            assertEquals(HomeUiState.Idle, awaitItem())

            viewModel.searchProducts(query)

            assertEquals(HomeUiState.Loading, awaitItem())

            val errorState = awaitItem()
            assertIs<HomeUiState.Error>(errorState)
            assertEquals(errorMessage, (errorState as HomeUiState.Error).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getProductDetails should update products state when use case returns success`() = runTest {
        val productId = "MLB123"
        val productDetail = ProductDetail(
            id = productId, title = "iPhone 13 Pro Max", imageUrls = listOf("http://example.com/image.png"),
            attributes = listOf(ProductAttribute("Marca", "Apple")),
            description = "Descrição detalhada."
        )
        coEvery { getProductsDetailUseCase(productId) } returns ResultWrapper.Success(productDetail)

        viewModel.getProductDetails(productId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(productDetail, viewModel.products.value)
        assertEquals(HomeUiState.Idle, viewModel.searchUiState.value)
    }


    @Test
    fun `getProductDetails should update state to Error when use case returns error`() = runTest {
        val productId = "MLB456"
        val errorMessage = "Invalid ID"
        coEvery { getProductsDetailUseCase(productId) } returns ResultWrapper.Error(errorMessage)

        viewModel.searchUiState.test {
            assertEquals(HomeUiState.Idle, awaitItem())

            viewModel.getProductDetails(productId)

            val errorState = awaitItem()
            assertIs<HomeUiState.Error>(errorState)
            assertEquals(errorMessage, (errorState as HomeUiState.Error).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onHistoryItemClicked should update query and trigger search`() = runTest {
        val historyQuery = "samsung"
        coEvery { getSearchProductUseCase(historyQuery) } returns flowOf(PagingData.empty())

        viewModel.onHistoryItemClicked(historyQuery)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(historyQuery, viewModel.searchQuery.value)
        coVerify { productRepository.saveSearchQuery(historyQuery) }
        coVerify { getSearchProductUseCase(historyQuery) }
    }
}