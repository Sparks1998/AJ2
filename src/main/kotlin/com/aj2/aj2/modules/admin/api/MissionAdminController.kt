package com.aj2.aj2.modules.admin.api

import com.aj2.aj2.modules.admin.application.MissionAdminService
import com.aj2.aj2.modules.mission.application.dto.CreateMissionTemplateRequest
import com.aj2.aj2.modules.mission.application.dto.MissionTemplateDto
import com.aj2.aj2.modules.mission.application.dto.UpdateMissionTemplateRequest
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
@RequestMapping("/mission-templates")
@PreAuthorize("hasRole('ADMIN')")
class MissionAdminController(
    private val missionAdminService: MissionAdminService,
) {
    @PostMapping
    fun create(@Valid @RequestBody request: CreateMissionTemplateRequest): MissionTemplateDto =
        missionAdminService.create(request)

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: UpdateMissionTemplateRequest): MissionTemplateDto =
        missionAdminService.update(id, request)
}
