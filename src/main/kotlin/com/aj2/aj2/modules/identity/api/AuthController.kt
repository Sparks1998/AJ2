package com.aj2.aj2.modules.identity.api

import com.aj2.aj2.modules.identity.application.AuthService
import com.aj2.aj2.modules.identity.application.dto.LoginRequest
import com.aj2.aj2.modules.identity.application.dto.LoginResponse
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest, response: HttpServletResponse): LoginResponse {
        val result = authService.login(request)
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer ${result.accessToken}")
        return LoginResponse(expiresAt = result.expiresAt, userId = result.userId, role = result.role)
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    fun logout(authentication: Authentication) {
        val jwtAuth = authentication as JwtAuthenticationToken
        val tokenId = UUID.fromString(jwtAuth.token.id)
        authService.logout(tokenId)
    }
}
