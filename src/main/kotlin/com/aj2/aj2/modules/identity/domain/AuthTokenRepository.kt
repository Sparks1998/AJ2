package com.aj2.aj2.modules.identity.domain

import java.util.UUID

interface AuthTokenRepository {
    fun findById(id: UUID): AuthToken?
    fun save(authToken: AuthToken): AuthToken
}
