package com.aj2.aj2.modules.job.application.dto

import jakarta.validation.constraints.NotBlank

data class CreateJobApplicationRequest(
    @field:NotBlank
    val source: String,
    @field:NotBlank
    val sourceRef: String,
    @field:NotBlank
    val title: String,
    val company: String? = null,
    @field:NotBlank
    val applicationUrl: String,
)
