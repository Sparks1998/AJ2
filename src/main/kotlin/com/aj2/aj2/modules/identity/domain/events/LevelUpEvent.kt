package com.aj2.aj2.modules.identity.domain.events

import java.util.UUID

/**
 * Raised whenever awardXp crosses one or more level thresholds in a single call.
 * Additional side effects (badges, etc.) hook in by listening for this event,
 * without awardXp itself needing to know about them.
 */
data class LevelUpEvent(
    val userId: UUID,
    val oldLevel: Int,
    val newLevel: Int,
    val newLevelTitle: String,
)
