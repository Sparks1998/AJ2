package com.aj2.aj2.modules.mission.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.mission.application.MissionService
import com.aj2.aj2.modules.mission.application.dto.CompleteMissionRequest
import com.aj2.aj2.modules.mission.application.dto.MissionCompletionDto
import com.aj2.aj2.modules.mission.application.dto.MissionTemplateDto
import com.aj2.aj2.modules.mission.domain.MissionPeriodType
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/missions")
@PreAuthorize("isAuthenticated()")
class MissionController(
    private val missionService: MissionService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @GetMapping
    fun list(@RequestParam period: MissionPeriodType): List<MissionTemplateDto> =
        missionService.listByPeriod(period)

    @PostMapping("/completions")
    fun complete(@Valid @RequestBody request: CompleteMissionRequest): MissionCompletionDto {
        val currentUser = currentUserResolver.current()
        return missionService.complete(currentUser.userId, request)
    }
}
