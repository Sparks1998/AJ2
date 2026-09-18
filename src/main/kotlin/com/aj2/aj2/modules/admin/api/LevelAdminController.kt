package com.aj2.aj2.modules.admin.api

import com.aj2.aj2.modules.admin.application.LevelAdminService
import com.aj2.aj2.modules.identity.application.dto.CreateLevelDefinitionRequest
import com.aj2.aj2.modules.identity.application.dto.LevelDefinitionDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Admin - Levels", description = "Admin management of gamification level definitions")
@RestController
@RequestMapping("/levels")
@PreAuthorize("hasRole('ADMIN')")
class LevelAdminController(
    private val levelAdminService: LevelAdminService,
) {
    @Operation(summary = "List all level definitions")
    @GetMapping
    fun list(): List<LevelDefinitionDto> = levelAdminService.list()

    @Operation(summary = "Create a new level definition")
    @PostMapping
    fun create(@Valid @RequestBody request: CreateLevelDefinitionRequest): LevelDefinitionDto =
        levelAdminService.create(request)
}
