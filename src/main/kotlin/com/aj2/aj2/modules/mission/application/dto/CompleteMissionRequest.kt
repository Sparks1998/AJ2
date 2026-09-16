package com.aj2.aj2.modules.mission.application.dto

import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.UUID

data class CompleteMissionRequest(
    @field:NotNull
    val id: UUID,
    @field:NotNull
    val missionTemplateId: UUID,
    @field:NotNull
    val completedAt: Instant,
)
