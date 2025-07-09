package com.giovanna.amatucci.melisearch.domain.usecase


import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import java.util.Base64

@ExperimentalCoroutinesApi
class GeneratePkceUseCaseTest {

    private lateinit var generatePkceUseCase: GeneratePkceUseCase

    @Before
    fun setUp() {
        generatePkceUseCase = GeneratePkceParamsUseCaseImpl()
    }

    @Test
    fun `invoke returns valid and non-empty PkceParams`() {
        val pkceParams = generatePkceUseCase()

        assertNotNull(pkceParams)

        assertTrue(pkceParams.codeVerifier.isNotBlank())
        assertTrue(pkceParams.codeChallenge.isNotBlank())
        assertEquals("S256", pkceParams.codeChallengeMethod)
    }

    @Test
    fun `invoke generates a codeChallenge that is a valid SHA-256 hash of the codeVerifier`() {
        val pkceParams = generatePkceUseCase()

        val verifierBytes = pkceParams.codeVerifier.toByteArray(Charsets.US_ASCII)
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(verifierBytes)
        val expectedChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest)

        assertEquals(expectedChallenge, pkceParams.codeChallenge)
    }
}