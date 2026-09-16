package com.aj2.aj2.modules.document.application.dto

import com.aj2.aj2.modules.document.domain.DocumentStatus
import jakarta.validation.constraints.NotNull

data class ReviewSubmissionRequest(
    @field:NotNull
    val decision: DocumentStatus,
    val comment: String? = null,
)
