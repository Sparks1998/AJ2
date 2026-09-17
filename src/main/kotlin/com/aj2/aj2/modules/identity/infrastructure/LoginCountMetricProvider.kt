package com.aj2.aj2.modules.identity.infrastructure

import com.aj2.aj2.modules.identity.domain.AuthTokenRepository
import com.aj2.aj2.modules.reward.application.RewardMetricProvider
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LoginCountMetricProvider(
    private val authTokenRepository: AuthTokenRepository,
) : RewardMetricProvider {
    override val ruleCodes = listOf("WELCOME")

    override fun currentValue(userId: UUID): Int = authTokenRepository.countByUserId(userId).toInt()
}
