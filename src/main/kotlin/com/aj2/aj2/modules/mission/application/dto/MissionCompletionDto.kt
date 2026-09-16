package com.aj2.aj2.modules.mission.application.dto

import java.time.Instant
import java.util.UUID

data class MissionCompletionDto(
    val id: UUID,
    val userId: UUID,
    val missionTemplateId: UUID,
    val xpAwarded: Int,
    val completedAt: Instant,
    val syncedAt: Instant,
)
