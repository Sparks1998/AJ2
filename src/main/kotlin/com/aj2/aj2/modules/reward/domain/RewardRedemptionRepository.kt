package com.aj2.aj2.modules.reward.domain

import java.util.UUID

interface RewardRedemptionRepository {
    fun findById(id: UUID): RewardRedemption?
    fun save(rewardRedemption: RewardRedemption): RewardRedemption
}
