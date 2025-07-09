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
class MeliApiImplTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var networkHttpClient: NetworkHttpClient

    private lateinit var meliApi: MeliApi

    @Test
    fun `searchProducts should return Success on 200 OK`() = runTest {
        val mockEngine = MockEngine { request ->
            val responseJson = """
        {
            "keywords": "iphone",
            "paging": {"total": 1, "limit": 1, "offset": 0},
            "results": [{"id": "MLB123"}],
            "used_attributes": [],
            "query_type": "test"
        }
    """.trimIndent()
            respond(
                content = responseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }
        meliApi = buildApiWithMockEngine(mockEngine)

        val result = meliApi.searchProducts("iphone", 0, 1)

        assertTrue(result is ApiResult.Success)
        assertEquals("iphone", (result as ApiResult.Success).data.keywords)
    }

    @Test
    fun `searchProducts should return Error on non-200 status`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.InternalServerError
            )
        }
        meliApi = buildApiWithMockEngine(mockEngine)

        val result = meliApi.searchProducts("iphone", 0, 1)

        assertTrue(result is ApiResult.Error)
        assertEquals("Internal Server Error", (result as ApiResult.Error).message)
    }

    @Test
    fun `getProductDetail should return Success on 200 OK`() = runTest {
        val productId = "MLB123"
        val mockEngine = MockEngine { request ->
            val responseJson = """
            {
                "id": "$productId",
                "catalog_product_id": "MLB6012825",
                "status": "active",
                "pdp_types": ["PRODUCT"],
                "domain_id": "MLB-CELLPHONES",
                "permalink": "https://www.mercadolivre.com.br/apple-iphone-11-64-gb-preto/p/MLB15149593",
                "name": "Apple iPhone 11 (64 GB) - Preto",
                "family_name": "iPhone 11",
                "type": "ITEM",
                "pictures": [],
                "attributes": [],
                "short_description": {
                    "type": "TEXT",
                    "content": "Grave vídeos 4K, faça belos retratos e capture paisagens inteiras com o novo sistema de câmera dupla."
                },
                "parent_id": null,
                "children_ids": [],
                "settings": null,
                "quality_type": "CATALOG_PRODUCT",
                "tags": [],
                "date_created": "2020-09-02T16:04:19.000Z",
                "last_updated": "2024-07-29T04:02:18.000Z"
            }
            """.trimIndent()
            respond(
                content = responseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }
        meliApi = buildApiWithMockEngine(mockEngine)

        val result = meliApi.getProductDetail(productId)

        assertTrue("O resultado deveria ser ApiResult.Success, mas foi ${result::class.simpleName}", result is ApiResult.Success)
        assertEquals(productId, (result as ApiResult.Success).data.id)
    }

    @Test
    fun `getProductDetail should return Error on 404 Not Found`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.NotFound
            )
        }
        meliApi = buildApiWithMockEngine(mockEngine)

        val result = meliApi.getProductDetail("non-existent-id")

        assertTrue(result is ApiResult.Error)
        assertEquals("Not Found", (result as ApiResult.Error).message)
    }

    private fun buildApiWithMockEngine(engine: MockEngine): MeliApi {
        val mockHttpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
        every { networkHttpClient.invoke() } returns mockHttpClient
        return MeliApiImpl(networkHttpClient)
    }
}
