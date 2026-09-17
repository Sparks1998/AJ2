package com.aj2.aj2.modules.reward.infrastructure

import com.aj2.aj2.modules.reward.domain.RewardRedemption
import com.aj2.aj2.modules.reward.domain.RewardRedemptionRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class RewardRedemptionRepositoryImpl(
    private val jpaRepository: RewardRedemptionJpaRepository,
) : RewardRedemptionRepository {
    override fun findById(id: UUID): RewardRedemption? = jpaRepository.findById(id).orElse(null)

    override fun findByUserId(userId: UUID): List<RewardRedemption> = jpaRepository.findByUserId(userId)

    override fun existsByUserIdAndRewardId(userId: UUID, rewardId: UUID): Boolean =
        jpaRepository.existsByUserIdAndRewardId(userId, rewardId)

    override fun save(rewardRedemption: RewardRedemption): RewardRedemption = jpaRepository.save(rewardRedemption)
}
