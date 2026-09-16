package com.aj2.aj2.modules.workshop.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant

data class CreateWorkshopRequest(
    @field:NotBlank
    val title: String,
    val description: String? = null,
    val location: String? = null,
    @field:NotNull
    val startsAt: Instant,
    val endsAt: Instant? = null,
)

data class UpdateWorkshopRequest(
    val title: String? = null,
    val description: String? = null,
    val location: String? = null,
    val startsAt: Instant? = null,
    val endsAt: Instant? = null,
)
