package com.aj2.aj2.modules.document.application.dto

import com.aj2.aj2.modules.document.domain.DocumentStatus
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class DocumentRequestDto(
    val id: UUID,
    val userId: UUID,
    val documentTypeId: UUID,
    val requestedBy: UUID,
    val documentMissionId: UUID?,
    val status: DocumentStatus,
    val note: String?,
    val requestedAt: Instant,
    val dueDate: LocalDate?,
)
