package com.aj2.aj2.modules.workshop.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.workshop.application.WorkshopService
import com.aj2.aj2.modules.workshop.application.dto.WorkshopDto
import com.aj2.aj2.modules.workshop.application.dto.WorkshopRegistrationDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Workshops", description = "Client workshop browsing and registration")
@RestController
@RequestMapping("/workshops")
class WorkshopController(
    private val workshopService: WorkshopService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @Operation(summary = "List workshops, optionally filtered to only upcoming ones")
    @GetMapping
    fun list(
        @Parameter(description = "If true, only workshops that haven't started yet")
        @RequestParam(required = false, defaultValue = "false")
        upcoming: Boolean,
    ): List<WorkshopDto> = workshopService.list(upcoming)

    @Operation(summary = "Register the caller for a workshop")
    @PostMapping("/{id}/register")
    fun register(@PathVariable id: UUID): WorkshopRegistrationDto {
        val currentUser = currentUserResolver.current()
        return workshopService.register(currentUser.userId, id)
    }

    @Operation(summary = "List workshops the caller is registered for")
    @GetMapping("/registered")
    fun registered(): List<WorkshopRegistrationDto> {
        val currentUser = currentUserResolver.current()
        return workshopService.listRegistered(currentUser.userId)
    }
}
