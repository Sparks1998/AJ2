package com.aj2.aj2.modules.identity.application.dto

import com.aj2.aj2.modules.identity.domain.UserRole
import java.time.Instant
import java.util.UUID

data class LoginResponse(
    val expiresAt: Instant,
    val userId: UUID,
    val role: UserRole,
)
