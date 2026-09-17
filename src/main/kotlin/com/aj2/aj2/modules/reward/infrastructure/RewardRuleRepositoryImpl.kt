package com.aj2.aj2.modules.reward.infrastructure

import com.aj2.aj2.modules.reward.domain.RewardRule
import com.aj2.aj2.modules.reward.domain.RewardRuleRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class RewardRuleRepositoryImpl(
    private val jpaRepository: RewardRuleJpaRepository,
) : RewardRuleRepository {
    override fun findById(id: UUID): RewardRule? = jpaRepository.findById(id).orElse(null)

    override fun findByCode(code: String): RewardRule? = jpaRepository.findByCode(code)
}
