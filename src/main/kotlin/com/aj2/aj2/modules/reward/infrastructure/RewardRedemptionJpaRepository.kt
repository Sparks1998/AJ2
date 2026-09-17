package com.aj2.aj2.modules.reward.infrastructure

import com.aj2.aj2.modules.reward.domain.RewardRedemption
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RewardRedemptionJpaRepository : JpaRepository<RewardRedemption, UUID> {
    fun findByUserId(userId: UUID): List<RewardRedemption>
    fun existsByUserIdAndRewardId(userId: UUID , rewardId: UUID): Boolean
}
