package com.aj2.aj2.modules.workshop.application.dto

import java.time.Instant
import java.util.UUID

data class WorkshopRegistrationDto(
    val id: UUID,
    val workshopId: UUID,
    val userId: UUID,
    val registeredAt: Instant,
    val attendedAt: Instant?,
)
