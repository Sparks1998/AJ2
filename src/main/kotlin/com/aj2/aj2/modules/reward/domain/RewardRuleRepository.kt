package com.aj2.aj2.modules.reward.domain

import java.util.UUID

interface RewardRuleRepository {
    fun findById(id: UUID): RewardRule?
    fun findByCode(code: String): RewardRule?
}
