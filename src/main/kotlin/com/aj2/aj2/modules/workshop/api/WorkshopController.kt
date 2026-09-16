package com.aj2.aj2.modules.workshop.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.workshop.application.WorkshopService
import com.aj2.aj2.modules.workshop.application.dto.WorkshopDto
import com.aj2.aj2.modules.workshop.application.dto.WorkshopRegistrationDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/workshops")
@PreAuthorize("isAuthenticated()")
class WorkshopController(
    private val workshopService: WorkshopService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @GetMapping
    fun list(@RequestParam(required = false, defaultValue = "false") upcoming: Boolean): List<WorkshopDto> =
        workshopService.list(upcoming)

    @PostMapping("/{id}/register")
    fun register(@PathVariable id: UUID): WorkshopRegistrationDto {
        val currentUser = currentUserResolver.current()
        return workshopService.register(currentUser.userId, id)
    }

    @GetMapping("/registered")
    fun registered(): List<WorkshopRegistrationDto> {
        val currentUser = currentUserResolver.current()
        return workshopService.listRegistered(currentUser.userId)
    }
}
