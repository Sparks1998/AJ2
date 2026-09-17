package com.aj2.aj2.modules.reward.application

import com.aj2.aj2.modules.reward.application.dto.RewardDto
import com.aj2.aj2.modules.reward.application.dto.RewardRedemptionDto
import com.aj2.aj2.modules.reward.domain.Reward
import com.aj2.aj2.modules.reward.domain.RewardRedemption
import com.aj2.aj2.modules.reward.domain.RewardRedemptionRepository
import com.aj2.aj2.modules.reward.domain.RewardRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RewardService(
    private val rewardRepository : RewardRepository ,
    private val rewardRedemptionRepository : RewardRedemptionRepository ,
) {
    fun list() : List<RewardDto> = rewardRepository.findAll().map { it.toDto() }

    fun listRedeemed(userId : UUID) : List<RewardRedemptionDto> =
        rewardRedemptionRepository.findByUserId(userId).map { it.toDto() }
}

internal fun Reward.toDto() = RewardDto(
    id = id !! ,
    title = title ,
    description = description ,
    imageUrl = imageUrl ,
    rewardRuleId = rewardRule.id !! ,
    ruleThreshold = ruleThreshold ,
    stock = stock ,
    active = active ,
)

internal fun RewardRedemption.toDto() = RewardRedemptionDto(
    id = id !! ,
    userId = user.id !! ,
    rewardId = reward.id !! ,
    status = status ,
    redeemedAt = redeemedAt ,
)
