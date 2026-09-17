package com.aj2.aj2.modules.reward.application

import java.util.UUID

/**
 * Computes the current value of one metric for a user (e.g. total job
 * applications). Any module can register a bean implementing this for one
 * or more ruleCodes - RewardTriggerAspect looks providers up by ruleCode at
 * runtime, so wiring a new metric never touches the aspect or
 * RewardGrantService. A single metric can back several rule codes (e.g. the
 * job application count backs both a "first apply" rule and a separate
 * "curious" rule at a higher threshold).
 */
interface RewardMetricProvider {
    val ruleCodes: List<String>
    fun currentValue(userId: UUID): Int
}
