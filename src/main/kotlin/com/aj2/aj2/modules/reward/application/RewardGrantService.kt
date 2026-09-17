package com.aj2.aj2.modules.reward.application

import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.reward.domain.RedemptionStatus
import com.aj2.aj2.modules.reward.domain.RewardRedemption
import com.aj2.aj2.modules.reward.domain.RewardRedemptionRepository
import com.aj2.aj2.modules.reward.domain.RewardRepository
import com.aj2.aj2.modules.reward.domain.RewardRuleRepository
import com.aj2.aj2.modules.reward.domain.events.RewardGrantedEvent
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * Entirely data-driven: which rewards exist, which rule they're tied to,
 * and their thresholds all come from the DB (reward_rules/rewards). This
 * service has no knowledge of what a ruleCode "means" - it's only ever
 * invoked by RewardTriggerAspect, never by application services directly.
 */
@Service
class RewardGrantService(
    private val rewardRuleRepository: RewardRuleRepository,
    private val rewardRepository: RewardRepository,
    private val rewardRedemptionRepository: RewardRedemptionRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {
    private val log = LoggerFactory.getLogger(RewardGrantService::class.java)

    @Transactional
    fun grantIfEligible(ruleCode: String, userId: UUID, currentValue: Int) {
        val rewardRule = rewardRuleRepository.findByCode(ruleCode)
        if (rewardRule == null) {
            log.warn("No reward rule configured for code '{}' - skipping evaluation", ruleCode)
            return
        }

        val eligibleRewards = rewardRepository
            .findByRewardRuleId(rewardRule.id!!)
            .filter { it.active && it.ruleThreshold <= currentValue }
        if (eligibleRewards.isEmpty()) return

        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")

        for (reward in eligibleRewards) {
            if (rewardRedemptionRepository.existsByUserIdAndRewardId(userId, reward.id!!)) continue

            reward.stock?.let { stock ->
                if (stock <= 0) continue
                reward.stock = stock - 1
                rewardRepository.save(reward)
            }

            rewardRedemptionRepository.save(
                RewardRedemption(
                    user = user,
                    reward = reward,
                    status = RedemptionStatus.FULFILLED,
                ),
            )

            eventPublisher.publishEvent(RewardGrantedEvent(userId, reward.id!!, reward.title))
        }
    }
}
