package com.aj2.aj2.modules.admin.application

import com.aj2.aj2.modules.reward.application.dto.CreateRewardRequest
import com.aj2.aj2.modules.reward.application.dto.RewardDto
import com.aj2.aj2.modules.reward.application.dto.RewardRedemptionDto
import com.aj2.aj2.modules.reward.application.dto.UpdateRedemptionStatusRequest
import com.aj2.aj2.modules.reward.application.dto.UpdateRewardRequest
import com.aj2.aj2.modules.reward.domain.Reward
import com.aj2.aj2.modules.reward.domain.RewardRedemptionRepository
import com.aj2.aj2.modules.reward.domain.RewardRepository
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class RewardAdminService(
    private val rewardRepository: RewardRepository,
    private val rewardRedemptionRepository: RewardRedemptionRepository,
) {
    @Transactional
    fun create(request: CreateRewardRequest): RewardDto {
        val saved = rewardRepository.save(
            Reward(
                title = request.title,
                description = request.description,
                xpCost = request.xpCost,
                stock = request.stock,
                active = request.active ?: true,
            ),
        )
        return saved.toDto()
    }

    @Transactional
    fun update(id: UUID, request: UpdateRewardRequest): RewardDto {
        val reward = rewardRepository.findById(id) ?: throw NotFoundException("Reward $id not found")

        request.title?.let { reward.title = it }
        request.description?.let { reward.description = it }
        request.xpCost?.let { reward.xpCost = it }
        request.stock?.let { reward.stock = it }
        request.active?.let { reward.active = it }

        return rewardRepository.save(reward).toDto()
    }

    @Transactional
    fun updateRedemptionStatus(id: UUID, request: UpdateRedemptionStatusRequest): RewardRedemptionDto {
        val redemption = rewardRedemptionRepository.findById(id)
            ?: throw NotFoundException("Reward redemption $id not found")
        redemption.status = request.status
        return rewardRedemptionRepository.save(redemption).toDto()
    }
}

private fun Reward.toDto() = RewardDto(
    id = id!!,
    title = title,
    description = description,
    xpCost = xpCost,
    stock = stock,
    active = active,
)

private fun com.aj2.aj2.modules.reward.domain.RewardRedemption.toDto() = RewardRedemptionDto(
    id = id!!,
    userId = user.id!!,
    rewardId = reward.id!!,
    xpSpent = xpSpent,
    status = status,
    redeemedAt = redeemedAt,
)
