package com.aj2.aj2.modules.identity.infrastructure

import com.aj2.aj2.modules.identity.domain.AuthToken
import com.aj2.aj2.modules.identity.domain.AuthTokenRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class AuthTokenRepositoryImpl(
    private val jpaRepository: AuthTokenJpaRepository,
) : AuthTokenRepository {
    override fun findById(id: UUID): AuthToken? = jpaRepository.findById(id).orElse(null)

    override fun countByUserId(userId: UUID): Long = jpaRepository.countByUser_Id(userId)

    override fun save(authToken: AuthToken): AuthToken = jpaRepository.save(authToken)

    override fun deleteById(id: UUID) = jpaRepository.deleteById(id)
}
