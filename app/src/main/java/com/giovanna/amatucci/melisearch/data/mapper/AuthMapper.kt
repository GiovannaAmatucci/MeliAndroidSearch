package com.giovanna.amatucci.melisearch.data.mapper

import com.giovanna.amatucci.melisearch.data.model.TokenEntity
import com.giovanna.amatucci.melisearch.data.model.TokenResponse

fun TokenResponse.toEntity() = TokenEntity(
    userId = this.userId,
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
    expiresIn = this.expiresIn,
    tokenType = this.tokenType,
    scope = this.scope
)