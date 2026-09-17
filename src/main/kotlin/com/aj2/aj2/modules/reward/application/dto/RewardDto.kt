package com.aj2.aj2.modules.reward.application.dto

import java.util.UUID

data class RewardDto(
    val id: UUID,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val rewardRuleId: UUID,
    val ruleThreshold: Int,
    val stock: Int?,
    val active: Boolean,
)
