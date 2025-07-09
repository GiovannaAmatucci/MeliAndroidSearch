package com.giovanna.amatucci.melisearch.data.api

import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.data.network.NetworkHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class AuthApiImplTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var networkHttpClient: NetworkHttpClient

    private lateinit var authApi: AuthApi

    private val validTokenResponseJson = """
    {
        "access_token": "test_access_token",
        "refresh_token": "test_refresh_token",
        "expires_in": 3600,
        "user_id": 12345,
        "token_type": "bearer",
        "scope": "read write"
    }
""".trimIndent()

    @Test
    fun `postAccessToken should return Success when server responds with 200 OK`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = validTokenResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }
        authApi = buildApiWithMockEngine(mockEngine)

        val result = authApi.postAccessToken("uri", "code", "verifier")

        assertTrue(result is ApiResult.Success)
        assertEquals("test_access_token", (result as ApiResult.Success).data.accessToken)
    }

    @Test
    fun `postAccessToken should return Error when server responds with non-200 status`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"error": "bad_request"}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }
        authApi = buildApiWithMockEngine(mockEngine)

        val result = authApi.postAccessToken("uri", "code", "verifier")

        assertTrue(result is ApiResult.Error)
        assertEquals("Bad Request", (result as ApiResult.Error).message)
    }

    @Test
    fun `refreshAccessToken should return Success when server responds with 200 OK`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = validTokenResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }
        authApi = buildApiWithMockEngine(mockEngine)

        val result = authApi.refreshAccessToken("id", "secret", "refresh_token")

        assertTrue(result is ApiResult.Success)
        assertEquals("test_refresh_token", (result as ApiResult.Success).data.refreshToken)
    }

    @Test
    fun `refreshAccessToken should return Error when server responds with non-200 status`() =
        runTest {
            val mockEngine = MockEngine { request ->
                respond(
                    content = """{"error": "invalid_grant"}""",
                    status = HttpStatusCode.Unauthorized,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                )
            }
            authApi = buildApiWithMockEngine(mockEngine)

            val result = authApi.refreshAccessToken("id", "secret", "refresh_token")

            assertTrue(result is ApiResult.Error)
            assertEquals("Unauthorized", (result as ApiResult.Error).message)
        }

    private fun buildApiWithMockEngine(engine: MockEngine): AuthApi {
        val mockHttpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
        every { networkHttpClient.invoke() } returns mockHttpClient
        return AuthApiImpl(networkHttpClient)
    }
}