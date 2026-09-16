package com.aj2.aj2.modules.reward.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.reward.application.RewardService
import com.aj2.aj2.modules.reward.application.dto.RewardDto
import com.aj2.aj2.modules.reward.application.dto.RewardRedemptionDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/rewards")
@PreAuthorize("isAuthenticated()")
class RewardController(
    private val rewardService: RewardService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @GetMapping
    fun list(): List<RewardDto> = rewardService.list()

    @PostMapping("/{id}/redeem")
    fun redeem(@PathVariable id: UUID): RewardRedemptionDto {
        val currentUser = currentUserResolver.current()
        return rewardService.redeem(currentUser.userId, id)
    }
}
