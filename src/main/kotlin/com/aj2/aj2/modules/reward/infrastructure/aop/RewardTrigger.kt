package com.aj2.aj2.modules.reward.infrastructure.aop

/**
 * Marks a use-case method as a point where one or more reward rules should
 * be re-evaluated after it completes successfully - the annotated method
 * itself never calls into the reward module. RewardTriggerAspect
 * intercepts every call, resolves the caller from the security context,
 * and RewardGrantService looks each ruleCode up dynamically against the
 * reward_rules table, so no application service ever references reward
 * logic directly.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RewardTrigger(vararg val ruleCodes: String)
