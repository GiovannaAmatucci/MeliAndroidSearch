package com.giovanna.amatucci.melisearch.presentation.features.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.giovanna.amatucci.melisearch.data.model.TokenResponse
import com.giovanna.amatucci.melisearch.domain.model.PkceParams
import com.giovanna.amatucci.melisearch.domain.repository.OauthRepository
import com.giovanna.amatucci.melisearch.domain.usecase.GeneratePkceUseCase
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import com.giovanna.amatucci.melisearch.presentation.features.login.state.LoginViewState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var repository: OauthRepository

    @RelaxedMockK
    private lateinit var useCase: GeneratePkceUseCase

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(repository, useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `preparePkceParams should generate pkce and update state`() = runTest {
        val pkceParams = PkceParams(
            codeVerifier = "verifier",
            codeChallenge = "challenge",
            codeChallengeMethod = "S256"
        )
        every { useCase() } returns pkceParams

        viewModel.preparePkceParams()

        Assert.assertNotNull(viewModel.pkceAuthUrlParams.value)
        Assert.assertEquals("challenge", viewModel.pkceAuthUrlParams.value?.codeChallenge)
        Assert.assertEquals("S256", viewModel.pkceAuthUrlParams.value?.codeChallengeMethod)
    }

    @Test
    fun `fetchAccessToken should update state to Success when repository returns success`() =
        runTest {
            val pkceParams = PkceParams("verifier", "challenge", "S256")
            val tokenResponse = TokenResponse("access", "refresh", 3600, 123, "bearer", "all")
            val successResult = ResultWrapper.Success(tokenResponse)
            val redirectUri = "uri"
            val code = "auth_code"

            every { useCase() } returns pkceParams
            coEvery {
                repository.postAccessToken(
                    redirectUri,
                    code,
                    "verifier"
                )
            } returns successResult

            viewModel.preparePkceParams()

            viewModel.state.test {
                Assert.assertEquals(LoginViewState.Loading, awaitItem())

                viewModel.fetchAccessToken(redirectUri, code)

                Assert.assertEquals(LoginViewState.Success, awaitItem())
            }
        }

    @Test
    fun `fetchAccessToken should update state to Error when repository returns error`() = runTest {
        val pkceParams = PkceParams("verifier", "challenge", "S256")
        val errorMessage = "Invalid code"
        val errorResult = ResultWrapper.Error<TokenResponse>(errorMessage)
        val redirectUri = "uri"
        val code = "auth_code"

        every { useCase() } returns pkceParams
        coEvery { repository.postAccessToken(redirectUri, code, "verifier") } returns errorResult

        viewModel.preparePkceParams()

        viewModel.state.test {
            Assert.assertEquals(LoginViewState.Loading, awaitItem())

            viewModel.fetchAccessToken(redirectUri, code)

            val finalState = awaitItem()
            Assert.assertTrue(finalState is LoginViewState.Error)
            Assert.assertEquals(errorMessage, (finalState as LoginViewState.Error).message)
        }
    }

    @Test
    fun `fetchAccessToken should update state to Error if code verifier is not present`() =
        runTest {
            val redirectUri = "uri"
            val code = "auth_code"

            viewModel.state.test {
                Assert.assertEquals(LoginViewState.Loading, awaitItem())

                viewModel.fetchAccessToken(redirectUri, code)

                val finalState = awaitItem()
                Assert.assertTrue(finalState is LoginViewState.Error)
                Assert.assertEquals(
                    "",
                    (finalState as LoginViewState.Error).message
                )
            }
        }
}