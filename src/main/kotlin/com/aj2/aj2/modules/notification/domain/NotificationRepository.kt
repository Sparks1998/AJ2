package com.aj2.aj2.modules.notification.domain

import java.util.UUID

interface NotificationRepository {
    fun findById(id: UUID): Notification?
    fun findByUserId(userId: UUID): List<Notification>
    fun save(notification: Notification): Notification
}
