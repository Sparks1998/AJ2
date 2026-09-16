package com.aj2.aj2.modules.identity.infrastructure

import com.aj2.aj2.modules.identity.domain.User
import com.aj2.aj2.modules.identity.domain.UserRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findById(id: UUID): User? = jpaRepository.findById(id).orElse(null)

    override fun findByEmail(email: String): User? = jpaRepository.findByEmail(email)

    override fun save(user: User): User = jpaRepository.save(user)
}
