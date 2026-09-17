package com.aj2.aj2.modules.identity.infrastructure

import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.reward.application.RewardMetricProvider
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class StreakMetricProvider(
    private val userRepository: UserRepository,
) : RewardMetricProvider {
    override val ruleCodes = listOf("STRIKES", "ACTIVE")

    override fun currentValue(userId: UUID): Int =
        (userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")).currentStreak
}
