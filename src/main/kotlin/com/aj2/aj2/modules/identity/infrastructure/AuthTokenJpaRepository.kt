package com.aj2.aj2.modules.identity.infrastructure

import com.aj2.aj2.modules.identity.domain.AuthToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuthTokenJpaRepository : JpaRepository<AuthToken, UUID> {
    fun countByUser_Id(userId: UUID): Long
}
