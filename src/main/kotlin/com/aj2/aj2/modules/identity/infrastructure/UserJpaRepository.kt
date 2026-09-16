package com.aj2.aj2.modules.identity.infrastructure

import com.aj2.aj2.modules.identity.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
}
