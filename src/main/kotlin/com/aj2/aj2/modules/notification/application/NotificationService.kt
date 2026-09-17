package com.aj2.aj2.modules.notification.application

import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.notification.application.dto.NotificationDto
import com.aj2.aj2.modules.notification.domain.DeviceTokenRepository
import com.aj2.aj2.modules.notification.domain.Notification
import com.aj2.aj2.modules.notification.domain.NotificationRepository
import com.aj2.aj2.modules.notification.infrastructure.FcmClient
import com.aj2.aj2.shared.exceptions.ForbiddenException
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.time.Instant
import java.util.UUID

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val deviceTokenRepository: DeviceTokenRepository,
    private val userRepository: UserRepository,
    private val fcmClient: FcmClient,
    private val objectMapper: ObjectMapper,
) {
    fun listForUser(userId: UUID): List<NotificationDto> =
        notificationRepository.findByUserId(userId).map { it.toDto() }

    @Transactional
    fun markRead(userId: UUID, notificationId: UUID): NotificationDto {
        val notification = notificationRepository.findById(notificationId)
            ?: throw NotFoundException("Notification $notificationId not found")

        if (notification.user.id != userId) {
            throw ForbiddenException("This notification does not belong to the caller")
        }

        if (notification.readAt == null) {
            notification.readAt = Instant.now()
            notificationRepository.save(notification)
        }

        return notification.toDto()
    }

    /**
     * [title]/[body] must be French - they are the fallback shown whenever the receiving
     * client can't localize the notification itself. [titleLocKey]/[bodyLocKey] (+
     * [bodyLocArgs]) are optional FCM localization keys so a client with matching string
     * resources can render the notification in the device's own language instead.
     */
    @Transactional
    fun notify(
        userId: UUID,
        type: String,
        title: String,
        body: String? = null,
        titleLocKey: String? = null,
        bodyLocKey: String? = null,
        bodyLocArgs: List<String>? = null,
    ) {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")

        val payload = if (titleLocKey != null || bodyLocKey != null) {
            objectMapper.writeValueAsString(
                mapOf(
                    "titleLocKey" to titleLocKey,
                    "bodyLocKey" to bodyLocKey,
                    "bodyLocArgs" to bodyLocArgs,
                ),
            )
        } else {
            null
        }

        notificationRepository.save(
            Notification(user = user, type = type, title = title, body = body, payload = payload),
        )

        val tokens = deviceTokenRepository.findByUserId(userId).map { it.fcmToken }
        fcmClient.push(tokens, title, body, titleLocKey, bodyLocKey, bodyLocArgs)
    }
}

private fun Notification.toDto() = NotificationDto(
    id = id!!,
    type = type,
    title = title,
    body = body,
    payload = payload,
    readAt = readAt,
    createdAt = createdAt!!,
)
