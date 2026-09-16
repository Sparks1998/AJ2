package com.aj2.aj2.modules.reward.application.dto

import com.aj2.aj2.modules.reward.domain.RedemptionStatus
import java.time.Instant
import java.util.UUID

data class RewardRedemptionDto(
    val id: UUID,
    val userId: UUID,
    val rewardId: UUID,
    val xpSpent: Int,
    val status: RedemptionStatus,
    val redeemedAt: Instant,
)
