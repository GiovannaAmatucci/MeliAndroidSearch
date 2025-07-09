package com.giovanna.amatucci.melisearch.data.repository

import com.giovanna.amatucci.melisearch.data.api.AuthApi
import com.giovanna.amatucci.melisearch.data.db.TokenDao
import com.giovanna.amatucci.melisearch.data.mapper.toEntity
import com.giovanna.amatucci.melisearch.data.model.TokenResponse
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
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OauthRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var meliApi: AuthApi

    @RelaxedMockK
    private lateinit var tokenDao: TokenDao

    private lateinit var coroutineDispatchers: CoroutineDispatchers

    private lateinit var repository: OauthRepositoryImpl

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        coroutineDispatchers = mockk {
            every { io() } returns testDispatcher
        }
        repository = OauthRepositoryImpl(meliApi, tokenDao, coroutineDispatchers)
    }

    @Test
    fun `postAccessToken should save token and return success when api call is successful`() = runTest(testScheduler) {
        val tokenResponse = TokenResponse("access", "refresh", 3600, 123L, "bearer", "all")
        val tokenEntity = tokenResponse.toEntity()
        val apiResult = ApiResult.Success(tokenResponse)
        coEvery { meliApi.postAccessToken(any(), any(), any()) } returns apiResult

        val result = repository.postAccessToken("uri", "code", "verifier")

        coVerify(exactly = 1) { tokenDao.saveToken(tokenEntity) }
        assertTrue(result is ResultWrapper.Success)
        assertEquals(tokenResponse, (result as ResultWrapper.Success).data)
    }

    @Test
    fun `postAccessToken should not save token and return error when api call is an error`() = runTest(testScheduler) {
        val errorMessage = "Unauthorized"
        val apiResult = ApiResult.Error(errorMessage)
        coEvery { meliApi.postAccessToken(any(), any(), any()) } returns apiResult

        val result = repository.postAccessToken("uri", "code", "verifier")

        coVerify(exactly = 0) { tokenDao.saveToken(any()) }
        assertTrue(result is ResultWrapper.Error)
        assertEquals(errorMessage, (result as ResultWrapper.Error).message)
    }

    @Test
    fun `postAccessToken should not save token and return error when api call is an exception`() = runTest(testScheduler) {
        val exception = RuntimeException("Network Error")
        val apiResult = ApiResult.Exception(exception)
        coEvery { meliApi.postAccessToken(any(), any(), any()) } returns apiResult

        val result = repository.postAccessToken("uri", "code", "verifier")

        coVerify(exactly = 0) { tokenDao.saveToken(any()) }
        assertTrue(result is ResultWrapper.Error)
        assertEquals(exception.localizedMessage, (result as ResultWrapper.Error).message)
    }
}
