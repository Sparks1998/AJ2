package com.aj2.aj2.modules.mission.application.dto

import com.aj2.aj2.modules.mission.domain.MissionCompletionType
import com.aj2.aj2.modules.mission.domain.MissionLinkedAction
import com.aj2.aj2.modules.mission.domain.MissionPeriodType
import java.util.UUID

data class MissionTemplateDto(
    val id: UUID,
    val title: String,
    val description: String?,
    val xpReward: Int,
    val periodType: MissionPeriodType,
    val completionType: MissionCompletionType,
    val linkedAction: MissionLinkedAction?,
    val targetCount: Int?,
    val active: Boolean,
)
