package com.aj2.aj2.modules.mission.application.dto

import com.aj2.aj2.modules.mission.domain.MissionCompletionType
import com.aj2.aj2.modules.mission.domain.MissionLinkedAction
import com.aj2.aj2.modules.mission.domain.MissionPeriodType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateMissionTemplateRequest(
    @field:NotBlank
    val title: String,
    val description: String? = null,
    @field:NotNull
    val xpReward: Int,
    @field:NotNull
    val periodType: MissionPeriodType,
    @field:NotNull
    val completionType: MissionCompletionType,
    val linkedAction: MissionLinkedAction? = null,
    val targetCount: Int? = null,
    val active: Boolean? = null,
)

data class UpdateMissionTemplateRequest(
    val title: String? = null,
    val description: String? = null,
    val xpReward: Int? = null,
    val periodType: MissionPeriodType? = null,
    val completionType: MissionCompletionType? = null,
    val linkedAction: MissionLinkedAction? = null,
    val targetCount: Int? = null,
    val active: Boolean? = null,
)
