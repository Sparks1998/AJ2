package com.aj2.aj2.modules.job.application.dto

import com.aj2.aj2.modules.job.domain.JobSource
import java.time.Instant
import java.util.UUID

data class JobApplicationDto(
    val id: UUID,
    val userId: UUID,
    val source: JobSource,
    val sourceRef: String,
    val title: String,
    val company: String?,
    val applicationUrl: String,
    val openedAt: Instant,
)
