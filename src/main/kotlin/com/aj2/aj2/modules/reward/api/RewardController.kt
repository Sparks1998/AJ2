package com.aj2.aj2.modules.reward.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.reward.application.RewardService
import com.aj2.aj2.modules.reward.application.dto.RewardDto
import com.aj2.aj2.modules.reward.application.dto.RewardRedemptionDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Rewards", description = "Gamification rewards and redemptions - auto-granted, never redeemed manually")
@RestController
@RequestMapping("/rewards")
class RewardController(
    private val rewardService: RewardService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @Operation(summary = "List all active rewards")
    @GetMapping
    fun list(): List<RewardDto> = rewardService.list()

    @Operation(summary = "List the caller's redeemed rewards")
    @GetMapping("/redeemed")
    fun redeemed(): List<RewardRedemptionDto> {
        val currentUser = currentUserResolver.current()
        return rewardService.listRedeemed(currentUser.userId)
    }
}
