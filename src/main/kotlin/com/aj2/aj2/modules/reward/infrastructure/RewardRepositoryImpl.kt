package com.aj2.aj2.modules.reward.infrastructure

import com.aj2.aj2.modules.reward.domain.Reward
import com.aj2.aj2.modules.reward.domain.RewardRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class RewardRepositoryImpl(
    private val jpaRepository: RewardJpaRepository,
) : RewardRepository {
    override fun findById(id: UUID): Reward? = jpaRepository.findById(id).orElse(null)

    override fun findAll(): List<Reward> = jpaRepository.findAll()

    override fun save(reward: Reward): Reward = jpaRepository.save(reward)
}
