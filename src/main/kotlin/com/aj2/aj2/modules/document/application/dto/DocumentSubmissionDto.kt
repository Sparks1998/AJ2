package com.aj2.aj2.modules.document.application.dto

import com.aj2.aj2.modules.document.domain.DocumentStatus
import java.time.Instant
import java.util.UUID

data class DocumentSubmissionDto(
    val id: UUID,
    val documentRequestId: UUID?,
    val documentMissionId: UUID?,
    val documentTypeId: UUID,
    val submittedBy: UUID,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val version: Int,
    val submittedAt: Instant,
    val reviewedBy: UUID?,
    val reviewedAt: Instant?,
    val reviewComment: String?,
    val decision: DocumentStatus?,
)
