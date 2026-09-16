package com.aj2.aj2.modules.document.application.dto

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

data class SubmitStandaloneRequest(
    val documentTypeId: UUID,
    val documentMissionId: UUID? = null,
    val file: MultipartFile,
)
