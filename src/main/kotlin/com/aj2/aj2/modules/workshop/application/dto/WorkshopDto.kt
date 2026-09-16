package com.aj2.aj2.modules.workshop.application.dto

import java.time.Instant
import java.util.UUID

data class WorkshopDto(
    val id: UUID,
    val title: String,
    val description: String?,
    val location: String?,
    val startsAt: Instant,
    val endsAt: Instant?,
    val createdBy: UUID,
)
