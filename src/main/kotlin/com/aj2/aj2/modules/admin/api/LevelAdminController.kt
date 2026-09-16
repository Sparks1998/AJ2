package com.aj2.aj2.modules.admin.api

import com.aj2.aj2.modules.admin.application.LevelAdminService
import com.aj2.aj2.modules.identity.application.dto.CreateLevelDefinitionRequest
import com.aj2.aj2.modules.identity.application.dto.LevelDefinitionDto
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/levels")
@PreAuthorize("hasRole('ADMIN')")
class LevelAdminController(
    private val levelAdminService: LevelAdminService,
) {
    @GetMapping
    fun list(): List<LevelDefinitionDto> = levelAdminService.list()

    @PostMapping
    fun create(@Valid @RequestBody request: CreateLevelDefinitionRequest): LevelDefinitionDto =
        levelAdminService.create(request)
}
