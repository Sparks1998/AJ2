package com.aj2.aj2.modules.reward.domain.events

import java.util.UUID

/**
 * Raised whenever a reward is auto-granted because the user met its rule
 * threshold. Additional side effects can hook in by listening for this
 * event without touching the granting logic itself.
 */
data class RewardGrantedEvent(
    val userId: UUID,
    val rewardId: UUID,
    val rewardTitle: String,
)
