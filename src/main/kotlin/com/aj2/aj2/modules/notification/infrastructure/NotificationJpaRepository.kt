package com.aj2.aj2.modules.notification.infrastructure

import com.aj2.aj2.modules.notification.domain.Notification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotificationJpaRepository : JpaRepository<Notification, UUID> {
    fun findByUserId(userId: UUID): List<Notification>
}
