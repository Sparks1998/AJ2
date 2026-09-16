package com.aj2.aj2.modules.identity.application

import com.aj2.aj2.modules.identity.domain.AuthToken
import com.aj2.aj2.modules.identity.domain.AuthTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

/**
 * Rotates the caller's token after a configurable number of requests when
 * rememberMe was false at login. AuthTokenFilter calls this on every
 * authenticated request; when it returns a new token, the filter sends it
 * back via the Authorization response header for the client's interceptor
 * to pick up.
 */
@Service
class TokenRotationService(
    private val authTokenRepository: AuthTokenRepository,
    private val jwtIssuer: JwtIssuer,
    @Value($$"${security.token.rotate-after-requests:20}") private val rotateAfterRequests: Int,
    @Value($$"${security.jwt.expiration-seconds}") private val expirationSeconds: Long,
) {
    @Transactional
    fun recordRequestAndMaybeRotate(authToken: AuthToken): String? {
        authToken.requestCount += 1

        if (authToken.rememberMe || authToken.requestCount < rotateAfterRequests) {
            authTokenRepository.save(authToken)
            return null
        }

        val now = Instant.now()
        authToken.revokedAt = now
        authTokenRepository.save(authToken)

        val expiresAt = now.plusSeconds(expirationSeconds)
        val newAuthToken = authTokenRepository.save(
            AuthToken(
                user = authToken.user,
                issuedAt = now,
                expiresAt = expiresAt,
                rememberMe = authToken.rememberMe,
                requestCount = 0,
            ),
        )

        return jwtIssuer.issue(newAuthToken.id!!, now, expiresAt)
    }
}
