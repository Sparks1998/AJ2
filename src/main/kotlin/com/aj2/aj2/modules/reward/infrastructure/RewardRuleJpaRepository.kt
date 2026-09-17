package com.aj2.aj2.modules.reward.infrastructure

import com.aj2.aj2.modules.reward.domain.RewardRule
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RewardRuleJpaRepository : JpaRepository<RewardRule, UUID> {
    fun findByCode(code: String): RewardRule?
}
