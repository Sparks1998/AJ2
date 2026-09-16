package com.aj2.aj2.modules.document.application.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

data class CreateDocumentRequestRequest(
    @field:NotNull
    val documentTypeId: UUID,
    val note: String? = null,
    val dueDate: LocalDate? = null,
)
