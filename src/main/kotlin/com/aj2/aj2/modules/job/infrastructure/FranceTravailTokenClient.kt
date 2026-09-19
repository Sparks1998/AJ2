package com.aj2.aj2.modules.job.infrastructure

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import java.time.Instant

/**
 * OAuth2 client-credentials token fetch for the France Travail API, cached
 * in memory until shortly before expiry. Credentials come from
 * application.yaml (france-travail.client-id/client-secret), which read
 * from FRANCE_TRAVAIL_CLIENT_ID/FRANCE_TRAVAIL_CLIENT_SECRET env vars -
 * nothing in this module ever reads the downloaded credentials file
 * directly.
 */
@Component
class FranceTravailTokenClient(
    private val authFeignClient: FranceTravailAuthFeignClient,
    @Value($$"${france-travail.client-id}") private val clientId: String,
    @Value($$"${france-travail.client-secret}") private val clientSecret: String,
    @Value($$"${france-travail.scope}") private val scope: String,
) {
    @Volatile
    private var cachedToken: String? = null

    @Volatile
    private var expiresAt: Instant = Instant.EPOCH

    fun getAccessToken(): String {
        cachedToken?.let { token -> if (Instant.now().isBefore(expiresAt)) return token }

        synchronized(this) {
            cachedToken?.let { token -> if (Instant.now().isBefore(expiresAt)) return token }

            val form = LinkedMultiValueMap<String, String>()
            form.add("grant_type", "client_credentials")
            form.add("client_id", clientId)
            form.add("client_secret", clientSecret)
            form.add("scope", scope)

            val response = authFeignClient.getToken(form)
            cachedToken = response.accessToken
            expiresAt = Instant.now().plusSeconds((response.expiresIn - 30).coerceAtLeast(0).toLong())
            return response.accessToken
        }
    }
}
