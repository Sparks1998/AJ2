package com.aj2.aj2.modules.identity.application

import com.aj2.aj2.modules.identity.application.dto.LoginRequest
import com.aj2.aj2.modules.identity.application.dto.LoginResult
import com.aj2.aj2.modules.identity.domain.AuthToken
import com.aj2.aj2.modules.identity.domain.AuthTokenRepository
import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.reward.infrastructure.aop.RewardTrigger
import com.aj2.aj2.shared.exceptions.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val authTokenRepository: AuthTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtIssuer: JwtIssuer,
    @Value($$"${security.jwt.expiration-seconds}") private val expirationSeconds: Long,
) {
    @Transactional
    @RewardTrigger("WELCOME")
    fun login(request: LoginRequest): LoginResult {
        val user = userRepository.findByEmail(request.email)
            ?: throw BadRequestException("Invalid email or password")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw BadRequestException("Invalid email or password")
        }

        // No JWT exists yet for this request, so AuthTokenFilter never runs - set the
        // context here so @RewardTrigger's CurrentUserResolver lookup resolves below.
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            user.id.toString(),
            null,
            listOf(SimpleGrantedAuthority("ROLE_${user.role}")),
        )

        val now = Instant.now()
        val expiresAt = now.plusSeconds(expirationSeconds)

        val authToken = authTokenRepository.save(
            AuthToken(user = user, issuedAt = now, expiresAt = expiresAt, rememberMe = request.rememberMe ?: false),
        )

        val accessToken = jwtIssuer.issue(authToken.id!!, now, expiresAt)

        return LoginResult(
            accessToken = accessToken,
            expiresAt = expiresAt,
            userId = user.id!!,
            role = user.role,
        )
    }

    @Transactional
    fun logout(tokenId: UUID) {
        val authToken = authTokenRepository.findById(tokenId) ?: return
        if (authToken.revokedAt == null) {
            authToken.revokedAt = Instant.now()
            authTokenRepository.save(authToken)
        }
    }
}
