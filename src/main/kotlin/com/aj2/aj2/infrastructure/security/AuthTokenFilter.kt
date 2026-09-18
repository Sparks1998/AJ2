package com.aj2.aj2.infrastructure.security

import com.aj2.aj2.modules.identity.application.TokenRotationService
import com.aj2.aj2.modules.identity.domain.AuthTokenRepository
import com.aj2.aj2.modules.identity.domain.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Instant
import java.util.UUID

/**
 * Runs right after Spring Security's OAuth2 Resource Server has already
 * verified the JWT signature and exp claim. This does the part that can
 * only be known from our own DB: cross-checks the token against auth_tokens
 * (revocation), re-resolves the caller's current role fresh from users, and
 * rotates the token after N requests. @PreAuthorize (method security) is
 * layered on top of this for role checks - it never does token validation.
 */
@Component
class AuthTokenFilter(
    private val authTokenRepository: AuthTokenRepository,
    private val userRepository: UserRepository,
    private val tokenRotationService: TokenRotationService,
    private val unauthorizedResponseWriter: UnauthorizedResponseWriter,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        val jwtAuth = authentication as? JwtAuthenticationToken
        if (jwtAuth == null) {
            filterChain.doFilter(request, response)
            return
        }

        val tokenId = jwtAuth.token.id
        if (tokenId.isNullOrBlank()) {
            unauthorizedResponseWriter.write(response, "No token ID found in authorization header")
            return
        }

        val authToken = runCatching { UUID.fromString(tokenId) }
            .getOrNull()
            ?.let { authTokenRepository.findById(it) }
        if (authToken == null) {
            unauthorizedResponseWriter.write(response, "Token not found")
            return
        }

        if (!authToken.isValid(Instant.now())) {
            unauthorizedResponseWriter.write(response, "Token expired or revoked")
            return
        }

        val user = authToken.user.id?.let { userRepository.findById(it) }
        if (user == null) {
            unauthorizedResponseWriter.write(response, "User not found")
            return
        }

        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
        val updated = JwtAuthenticationToken(jwtAuth.token, authorities, user.id.toString())
        SecurityContextHolder.getContext().authentication = updated

        val rotatedToken = tokenRotationService.recordRequestAndMaybeRotate(authToken)
        if (rotatedToken != null) {
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer $rotatedToken")
        }

        filterChain.doFilter(request, response)
    }
}
