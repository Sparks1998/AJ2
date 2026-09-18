package com.aj2.aj2.modules.identity.api

import com.aj2.aj2.modules.identity.application.AuthService
import com.aj2.aj2.modules.identity.application.dto.LoginRequest
import com.aj2.aj2.modules.identity.application.dto.UserDto
import com.aj2.aj2.modules.identity.application.dto.toDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Auth", description = "Login and logout")
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @Operation(
        summary = "Log in",
        description = "Returns the caller's profile in the body; the access token is returned via the " +
            "Authorization response header, never in the body.",
    )
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest, response: HttpServletResponse): UserDto {
        val result = authService.login(request)
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer ${result.accessToken}")
        return result.user.toDto()
    }

    @Operation(summary = "Log out and revoke the current session's token")
    @PostMapping("/logout")
    fun logout(authentication: Authentication) {
        val jwtAuth = authentication as JwtAuthenticationToken
        val tokenId = UUID.fromString(jwtAuth.token.id)
        authService.logout(tokenId)
    }
}
