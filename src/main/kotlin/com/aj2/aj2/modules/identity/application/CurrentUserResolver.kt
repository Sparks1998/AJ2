package com.aj2.aj2.modules.identity.application

import com.aj2.aj2.modules.identity.domain.UserRole
import com.aj2.aj2.shared.exceptions.ForbiddenException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CurrentUserResolver {
    fun current(): AuthenticatedUser {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw ForbiddenException("No authenticated user in context")

        val userId = runCatching { UUID.fromString(authentication.name) }
            .getOrNull()
            ?: throw ForbiddenException("No authenticated user in context")

        val role = authentication.authorities
            .mapNotNull { it.authority?.removePrefix("ROLE_") }
            .firstNotNullOfOrNull { authority ->
                runCatching { UserRole.valueOf(authority) }.getOrNull()
            }
            ?: throw ForbiddenException("Authenticated user has no role")

        return AuthenticatedUser(userId, role)
    }
}
