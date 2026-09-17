package com.aj2.aj2.modules.reward.infrastructure.aop

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.reward.application.RewardGrantService
import com.aj2.aj2.modules.reward.application.RewardMetricProvider
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Generic interceptor for every @RewardTrigger-annotated method in the
 * app. This is the only place a reward auto-grant is ever triggered from -
 * application services stay completely unaware of rewards.
 */
@Aspect
@Component
class RewardTriggerAspect(
    metricProviders: List<RewardMetricProvider>,
    private val rewardGrantService: RewardGrantService,
    private val currentUserResolver: CurrentUserResolver,
) {
    private val log = LoggerFactory.getLogger(RewardTriggerAspect::class.java)
    private val providersByRuleCode = metricProviders
        .flatMap { provider -> provider.ruleCodes.map { it to provider } }
        .toMap()

    @AfterReturning("@annotation(com.aj2.aj2.modules.reward.infrastructure.aop.RewardTrigger)")
    fun afterTriggeringMethod(joinPoint: JoinPoint) {
        val method = (joinPoint.signature as MethodSignature).method
        val ruleCodes = method.getAnnotation(RewardTrigger::class.java).ruleCodes
        val userId = currentUserResolver.current().userId

        for (ruleCode in ruleCodes) {
            val provider = providersByRuleCode[ruleCode]
            if (provider == null) {
                log.warn("No RewardMetricProvider registered for ruleCode '{}' - skipping", ruleCode)
                continue
            }
            rewardGrantService.grantIfEligible(ruleCode, userId, provider.currentValue(userId))
        }
    }
}
