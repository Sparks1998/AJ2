package com.aj2.aj2.modules.job.application.dto

import com.aj2.aj2.modules.job.domain.JobSource
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateJobApplicationRequest(
    @field:NotNull
    var source: JobSource,
    @field:NotBlank
    val sourceRef: String,
    @field:NotBlank
    val title: String,
    val company: String? = null,
    @field:NotBlank
    val applicationUrl: String,
)
