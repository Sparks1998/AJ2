package com.aj2.aj2.modules.mission.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.mission.application.MissionService
import com.aj2.aj2.modules.mission.application.dto.CompleteMissionRequest
import com.aj2.aj2.modules.mission.application.dto.MissionCompletionDto
import com.aj2.aj2.modules.mission.application.dto.MissionTemplateDto
import com.aj2.aj2.modules.mission.domain.MissionPeriodType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Missions", description = "Gamified mission templates and completions")
@RestController
@RequestMapping("/missions")
class MissionController(
    private val missionService: MissionService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @Operation(summary = "List mission templates for a given period (daily/weekly/etc.)")
    @GetMapping
    fun list(@RequestParam period: MissionPeriodType): List<MissionTemplateDto> =
        missionService.listByPeriod(period)

    @Operation(summary = "Complete a mission for the caller, awarding XP and updating the activity streak")
    @PostMapping("/completions")
    fun complete(@Valid @RequestBody request: CompleteMissionRequest): MissionCompletionDto {
        val currentUser = currentUserResolver.current()
        return missionService.complete(currentUser.userId, request)
    }
}
