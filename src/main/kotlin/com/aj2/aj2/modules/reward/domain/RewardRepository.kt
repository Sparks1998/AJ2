package com.aj2.aj2.modules.reward.domain

import java.util.UUID

interface RewardRepository {
    fun findById(id: UUID): Reward?
    fun findAll(): List<Reward>
    fun findByRewardRuleId(rewardRuleId: UUID): List<Reward>
    fun save(reward: Reward): Reward
}
