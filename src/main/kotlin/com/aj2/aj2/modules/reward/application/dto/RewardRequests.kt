package com.aj2.aj2.modules.reward.application.dto

import com.aj2.aj2.modules.reward.domain.RedemptionStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateRewardRequest(
    @field:NotBlank
    val title: String,
    val description: String? = null,
    @field:NotNull
    val xpCost: Int,
    val stock: Int? = null,
    val active: Boolean? = null,
)

data class UpdateRewardRequest(
    val title: String? = null,
    val description: String? = null,
    val xpCost: Int? = null,
    val stock: Int? = null,
    val active: Boolean? = null,
)

data class UpdateRedemptionStatusRequest(
    @field:NotNull
    val status: RedemptionStatus,
)
