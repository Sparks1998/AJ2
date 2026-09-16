package com.aj2.aj2.modules.identity.application.dto

import com.aj2.aj2.modules.identity.domain.UserRole
import java.time.Instant
import java.util.UUID

/**
 * Internal result of a login, used only by the API layer to set the
 * Authorization response header. The JSON body never carries the token
 * (see LoginResponse) - clients read it from the header, same as every
 * subsequent rotated token.
 */
data class LoginResult(
    val accessToken: String,
    val expiresAt: Instant,
    val userId: UUID,
    val role: UserRole,
)
