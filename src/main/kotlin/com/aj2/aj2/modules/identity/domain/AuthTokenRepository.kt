package com.aj2.aj2.modules.identity.domain

import java.util.UUID

interface AuthTokenRepository {
    fun findById(id: UUID): AuthToken?
    fun countByUserId(userId: UUID): Long
    fun save(authToken: AuthToken): AuthToken
    fun deleteById(id: UUID)
}
