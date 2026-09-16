package com.aj2.aj2.modules.identity.application

import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

/**
 * Single place that mints a JWT for an auth_token row (jti = token id,
 * plus standard iat/exp - no other claims). Used both at login and
 * whenever AuthTokenFilter rotates a token mid-session.
 */
@Component
class JwtIssuer(
    private val jwtEncoder: JwtEncoder,
) {
    fun issue(tokenId: UUID, issuedAt: Instant, expiresAt: Instant): String {
        val jwt = jwtEncoder.encode(
            JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                    .id(tokenId.toString())
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .build(),
            ),
        )
        return jwt.tokenValue
    }
}
