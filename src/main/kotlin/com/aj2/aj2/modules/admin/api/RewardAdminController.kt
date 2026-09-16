package com.aj2.aj2.modules.admin.api

import com.aj2.aj2.modules.admin.application.RewardAdminService
import com.aj2.aj2.modules.reward.application.dto.CreateRewardRequest
import com.aj2.aj2.modules.reward.application.dto.RewardDto
import com.aj2.aj2.modules.reward.application.dto.RewardRedemptionDto
import com.aj2.aj2.modules.reward.application.dto.UpdateRedemptionStatusRequest
import com.aj2.aj2.modules.reward.application.dto.UpdateRewardRequest
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/rewards")
@PreAuthorize("hasRole('ADMIN')")
class RewardAdminController(
    private val rewardAdminService: RewardAdminService,
) {
    @PostMapping
    fun create(@Valid @RequestBody request: CreateRewardRequest): RewardDto = rewardAdminService.create(request)

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: UpdateRewardRequest): RewardDto =
        rewardAdminService.update(id, request)

    @PatchMapping("/redemptions/{id}/status")
    fun updateRedemptionStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateRedemptionStatusRequest,
    ): RewardRedemptionDto = rewardAdminService.updateRedemptionStatus(id, request)
}
