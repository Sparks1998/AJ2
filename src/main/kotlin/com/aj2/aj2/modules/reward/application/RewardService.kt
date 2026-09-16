package com.aj2.aj2.modules.reward.application

import com.aj2.aj2.modules.identity.application.UserXpService
import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.reward.application.dto.RewardDto
import com.aj2.aj2.modules.reward.application.dto.RewardRedemptionDto
import com.aj2.aj2.modules.reward.domain.RedemptionStatus
import com.aj2.aj2.modules.reward.domain.RewardRedemption
import com.aj2.aj2.modules.reward.domain.RewardRedemptionRepository
import com.aj2.aj2.modules.reward.domain.RewardRepository
import com.aj2.aj2.shared.exceptions.BadRequestException
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class RewardService(
    private val rewardRepository: RewardRepository,
    private val rewardRedemptionRepository: RewardRedemptionRepository,
    private val userRepository: UserRepository,
    private val userXpService: UserXpService,
) {
    fun list(): List<RewardDto> = rewardRepository.findAll().map { it.toDto() }

    @Transactional
    fun redeem(userId: UUID, rewardId: UUID): RewardRedemptionDto {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")
        val reward = rewardRepository.findById(rewardId) ?: throw NotFoundException("Reward $rewardId not found")

        if (!reward.active) {
            throw BadRequestException("Reward $rewardId is not active")
        }
        if (user.xpTotal < reward.xpCost) {
            throw BadRequestException("Not enough XP to redeem this reward")
        }
        reward.stock?.let { stock ->
            if (stock <= 0) throw BadRequestException("Reward $rewardId is out of stock")
            reward.stock = stock - 1
            rewardRepository.save(reward)
        }

        userXpService.deductXp(userId, reward.xpCost)

        val redemption = rewardRedemptionRepository.save(
            RewardRedemption(
                user = user,
                reward = reward,
                xpSpent = reward.xpCost,
                status = RedemptionStatus.PENDING,
            ),
        )

        return redemption.toDto()
    }
}

internal fun com.aj2.aj2.modules.reward.domain.Reward.toDto() = RewardDto(
    id = id!!,
    title = title,
    description = description,
    xpCost = xpCost,
    stock = stock,
    active = active,
)

internal fun RewardRedemption.toDto() = RewardRedemptionDto(
    id = id!!,
    userId = user.id!!,
    rewardId = reward.id!!,
    xpSpent = xpSpent,
    status = status,
    redeemedAt = redeemedAt,
)
