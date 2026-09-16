package com.aj2.aj2.modules.notification.application.dto

import java.time.Instant
import java.util.UUID

data class NotificationDto(
    val id: UUID,
    val type: String,
    val title: String,
    val body: String?,
    val payload: String?,
    val readAt: Instant?,
    val createdAt: Instant,
)
