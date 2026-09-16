package com.aj2.aj2.modules.job.application.dto

import java.time.Instant
import java.util.UUID

data class JobApplicationDto(
    val id: UUID,
    val userId: UUID,
    val source: String,
    val sourceRef: String,
    val title: String,
    val company: String?,
    val applicationUrl: String,
    val openedAt: Instant,
)
