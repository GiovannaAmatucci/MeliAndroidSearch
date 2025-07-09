package com.giovanna.amatucci.melisearch.data.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giovanna.amatucci.melisearch.data.api.MeliApi
import com.giovanna.amatucci.melisearch.data.model.Paging
import com.giovanna.amatucci.melisearch.data.model.SearchResponse
import com.giovanna.amatucci.melisearch.data.model.SearchResultItem
import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.domain.model.Products
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs
import kotlin.test.assertNull


@ExperimentalCoroutinesApi
class ProductPagingSourceTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var meliApi: MeliApi

    private lateinit var pagingSource: ProductPagingSource

    @Before
    fun setUp() {
        pagingSource = ProductPagingSource(meliApi, "test_query")
    }

    @Test
    fun `load returns page when api call is successful`() = runTest {
        // Given
        // Given
        val searchResponse = SearchResponse(
            keywords = "query",
            paging = Paging(total = 100, limit = 20, offset = 0),
            results = listOf(
                SearchResultItem(id = "1", name = "Product 1"),
                SearchResultItem(id = "2", name = "Product 2")
            ),
            usedAttributes = emptyList(),
            queryType = "test"
        )
        coEvery { meliApi.searchProducts(any(), any(), any()) } returns ApiResult.Success(
            searchResponse
        )

        // When
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(2, page.data.size)
        assertNull(page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load returns error when api returns no results`() = runTest {
        // Given
        val emptyResponse = SearchResponse(
            keywords = "query",
            paging = Paging(total = 0, limit = 20, offset = 0),
            results = emptyList(),
            usedAttributes = emptyList(),
            queryType = "test"
        )
        coEvery { meliApi.searchProducts(any(), any(), any()) } returns ApiResult.Success(
            emptyResponse
        )

        // When
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertIs<PagingSource.LoadResult.Error<Int, Products>>(loadResult)
        val error = loadResult as PagingSource.LoadResult.Error
        assertNull(error.throwable.message)
    }

    @Test
    fun `load returns error when api call fails`() = runTest {
        // Given
        val errorMessage = "Network Error"
        coEvery {
            meliApi.searchProducts(
                any(),
                any(),
                any()
            )
        } returns ApiResult.Error(errorMessage)

        // When
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(loadResult is PagingSource.LoadResult.Error)
    }

    @Test
    fun `getRefreshKey returns anchorPosition is null or pages are empty`() {
        // Given an empty state
        val pagingState = PagingState<Int, Products>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val refreshKey = pagingSource.getRefreshKey(pagingState)

        // Then
        assertNull(refreshKey)
    }

}
