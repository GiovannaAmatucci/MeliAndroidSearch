package com.giovanna.amatucci.melisearch.data.repository

import com.giovanna.amatucci.melisearch.data.api.AuthApi
import com.giovanna.amatucci.melisearch.data.db.TokenDao
import com.giovanna.amatucci.melisearch.data.mapper.toEntity
import com.giovanna.amatucci.melisearch.data.model.TokenResponse
import com.giovanna.amatucci.melisearch.data.network.ApiResult
import com.giovanna.amatucci.melisearch.di.CoroutineDispatchers
import com.giovanna.amatucci.melisearch.domain.repository.OauthRepository
import com.giovanna.amatucci.melisearch.domain.util.ResultWrapper
import kotlinx.coroutines.withContext

class OauthRepositoryImpl(
    private val meliApi: AuthApi,
    private val tokenDao: TokenDao,
    private val ioDispatchers: CoroutineDispatchers
): OauthRepository {
    override suspend fun postAccessToken(
        redirectUri: String,
        code: String,
        codeVerifier: String
    ): ResultWrapper<TokenResponse> = withContext(ioDispatchers.io()) {
        try {
            val response = meliApi.postAccessToken(redirectUri, code, codeVerifier)
            when (response) {
                is ApiResult.Success -> {
                    tokenDao.saveToken(response.data.toEntity())
                    ResultWrapper.Success(response.data)
                }
                is ApiResult.Error -> {
                    ResultWrapper.Error(response.message)
                }
                is ApiResult.Exception -> {
                    ResultWrapper.Error(response.exception.localizedMessage.orEmpty())
                }
            }
        } catch (t: Throwable) {
            ResultWrapper.Error(t.localizedMessage.orEmpty())
        }
    }
}