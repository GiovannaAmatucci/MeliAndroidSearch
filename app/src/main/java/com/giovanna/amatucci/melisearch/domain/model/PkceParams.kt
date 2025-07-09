package com.giovanna.amatucci.melisearch.domain.model

data class PkceParams(
    val codeVerifier: String,
    val codeChallenge: String,
    val codeChallengeMethod: String = "S256"
)

data class PkceAuthUrlParams(
    val codeChallenge: String,
    val codeChallengeMethod: String
)