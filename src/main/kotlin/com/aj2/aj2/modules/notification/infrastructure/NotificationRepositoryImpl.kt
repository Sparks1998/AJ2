package com.aj2.aj2.modules.notification.infrastructure

import com.aj2.aj2.modules.notification.domain.Notification
import com.aj2.aj2.modules.notification.domain.NotificationRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class NotificationRepositoryImpl(
    private val jpaRepository: NotificationJpaRepository,
) : NotificationRepository {
    override fun findById(id: UUID): Notification? = jpaRepository.findById(id).orElse(null)

    override fun findByUserId(userId: UUID): List<Notification> = jpaRepository.findByUserId(userId)

    override fun save(notification: Notification): Notification = jpaRepository.save(notification)
}
