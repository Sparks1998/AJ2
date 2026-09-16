package com.aj2.aj2.modules.identity.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateLevelDefinitionRequest(
    @field:NotNull
    val level: Int,
    @field:NotNull
    val xpRequired: Int,
    @field:NotBlank
    val title: String,
)
