package com.aj2.aj2.modules.notification.infrastructure

import com.aj2.aj2.modules.notification.domain.DeviceToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DeviceTokenJpaRepository : JpaRepository<DeviceToken, UUID> {
    fun findByUserId(userId: UUID): List<DeviceToken>
}
