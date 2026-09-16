package com.aj2.aj2.modules.identity.application

import com.aj2.aj2.modules.identity.domain.UserRole
import java.util.UUID

data class AuthenticatedUser(
    val userId: UUID,
    val role: UserRole,
)
