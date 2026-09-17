package com.aj2.aj2.modules.reward.application.dto

import com.aj2.aj2.modules.reward.domain.RedemptionStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateRewardRequest(
    @field:NotBlank
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    @field:NotNull
    var rewardRuleId: UUID,
    @field:NotNull
    var ruleThreshold: Int,
    val stock: Int? = null,
    val active: Boolean? = null,
)

data class UpdateRewardRequest(
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val rewardRuleId: UUID? = null,
    val ruleThreshold: Int? = null,
    val stock: Int? = null,
    val active: Boolean? = null,
)

data class UpdateRedemptionStatusRequest(
    @field:NotNull
    var status: RedemptionStatus,
)
