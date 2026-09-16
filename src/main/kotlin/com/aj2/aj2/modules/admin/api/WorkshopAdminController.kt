package com.aj2.aj2.modules.admin.api

import com.aj2.aj2.modules.admin.application.WorkshopAdminService
import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.workshop.application.dto.CreateWorkshopRequest
import com.aj2.aj2.modules.workshop.application.dto.UpdateWorkshopRequest
import com.aj2.aj2.modules.workshop.application.dto.WorkshopDto
import com.aj2.aj2.modules.workshop.application.dto.WorkshopRegistrationDto
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
@RequestMapping("/workshops")
@PreAuthorize("hasRole('ADMIN')")
class WorkshopAdminController(
    private val workshopAdminService: WorkshopAdminService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @PostMapping
    fun create(@Valid @RequestBody request: CreateWorkshopRequest): WorkshopDto {
        val currentUser = currentUserResolver.current()
        return workshopAdminService.create(currentUser.userId, request)
    }

    @PatchMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody request: UpdateWorkshopRequest): WorkshopDto =
        workshopAdminService.update(id, request)

    @PostMapping("/{id}/attendees/{userId}/mark-attended")
    fun markAttended(@PathVariable id: UUID, @PathVariable userId: UUID): WorkshopRegistrationDto =
        workshopAdminService.markAttended(id, userId)
}
