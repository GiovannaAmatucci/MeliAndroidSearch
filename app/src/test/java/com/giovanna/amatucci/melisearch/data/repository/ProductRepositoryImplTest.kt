package com.giovanna.amatucci.melisearch.data.repository

import androidx.paging.PagingData
import com.giovanna.amatucci.melisearch.data.api.MeliApi
import com.giovanna.amatucci.melisearch.data.db.SearchDao
import com.giovanna.amatucci.melisearch.data.model.AttributeDetail
import com.giovanna.amatucci.melisearch.data.model.PictureDetail
import com.giovanna.amatucci.melisearch.data.model.ProductDetailResponse
import com.giovanna.amatucci.melisearch.data.model.SearchEntity
import com.giovanna.amatucci.melisearch.data.model.ShortDescription
import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.di.CoroutineDispatchers
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var meliApi: MeliApi

    @RelaxedMockK
    private lateinit var searchDao: SearchDao

    private lateinit var coroutineDispatchers: CoroutineDispatchers

    private lateinit var repository: ProductRepositoryImpl

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        coroutineDispatchers = mockk {
            every { io() } returns testDispatcher
            every { default() } returns testDispatcher
        }
        repository = ProductRepositoryImpl(meliApi, searchDao, coroutineDispatchers)
    }

    @Test
    fun `getProductsStream should also save search query`() = runTest(testScheduler) {
        // Given
        val query = "test query"
        coEvery { searchDao.getSearchHistory() } returns null

        repository.getProductsStream(query).first()

        coVerify { searchDao.insertSearch(any()) }
    }

    @Test
    fun `saveSearchQuery should add new query to top of history`() = runTest(testScheduler) {
        // Given
        val existingHistory = SearchEntity(id = 1, queries = listOf("iphone", "galaxy"))
        val newQuery = "motorola"
        coEvery { searchDao.getSearchHistory() } returns existingHistory

        // When
        repository.saveSearchQuery(newQuery)

        // Then
        coVerify {
            searchDao.insertSearch(
                match {
                    it.queries?.first() == newQuery && it.queries?.size == 3
                }
            )
        }
    }

    @Test
    fun `saveSearchQuery should not save blank queries`() = runTest(testScheduler) {
        // Given
        val blankQuery = "   "

        // When
        repository.saveSearchQuery(blankQuery)

        // Then
        // Verifica que o insert NUNCA foi chamado
        coVerify(exactly = 0) { searchDao.insertSearch(any()) }
    }

    @Test
    fun `getSearchQueries should return history from DAO`() = runTest(testScheduler) {
        // Given
        val expectedQueries = listOf("query1", "query2")
        val historyEntity = SearchEntity(id = 1, queries = expectedQueries)
        coEvery { searchDao.getSearchHistory() } returns historyEntity

        // When
        val result = repository.getSearchQueries()

        // Then
        assertEquals(expectedQueries, result)
    }

    @Test
    fun `getSearchQueries should return empty list when history is null`() = runTest(testScheduler) {
        // Given
        coEvery { searchDao.getSearchHistory() } returns null

        // When
        val result = repository.getSearchQueries()

        // Then
        assertTrue(result.isEmpty())
    }
}

