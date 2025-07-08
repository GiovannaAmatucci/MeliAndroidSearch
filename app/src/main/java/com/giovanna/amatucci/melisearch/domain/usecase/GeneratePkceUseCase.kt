package com.giovanna.amatucci.melisearch.domain.usecase

import com.giovanna.amatucci.melisearch.domain.model.PkceParams
import com.giovanna.amatucci.melisearch.domain.repository.OauthRepository
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * Use case para gerar os parâmetros necessários para o fluxo PKCE.
 */
interface GeneratePkceUseCase {
    /**
     * Gera um novo conjunto de parâmetros PKCE.
     * O code_verifier gerado deve ser armazenado de forma segura pelo chamador
     * para ser usado posteriormente na troca do código de autorização pelo token.
     *
     * @return [PkceParams] contendo o codeVerifier, codeChallenge e codeChallengeMethod.
     */
    operator fun invoke(): PkceParams
}

class GeneratePkceParamsUseCaseImpl() : GeneratePkceUseCase {

    override operator fun invoke(): PkceParams {
        val codeVerifier = generateCodeVerifier()
        val codeChallenge = generateCodeChallenge(codeVerifier)
        return PkceParams(
            codeVerifier = codeVerifier,
            codeChallenge = codeChallenge,
            codeChallengeMethod = CODE_CHALLENGE_METHOD
        )
    }

  private fun generateCodeVerifier(): String {
        val sr = SecureRandom()
        val code = ByteArray(32)
        sr.nextBytes(code)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code)
    }

   private fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(Charsets.US_ASCII)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
    companion object {
        const val CODE_CHALLENGE_METHOD = "S256"
    }
}