package com.aj2.aj2.modules.notification.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.notification.application.NotificationService
import com.aj2.aj2.modules.notification.application.dto.NotificationDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Notifications", description = "In-app notifications")
@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationService: NotificationService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @Operation(summary = "List the caller's notifications")
    @GetMapping
    fun list(): List<NotificationDto> {
        val currentUser = currentUserResolver.current()
        return notificationService.listForUser(currentUser.userId)
    }

    @Operation(summary = "Mark a notification as read")
    @PatchMapping("/{id}/read")
    fun markRead(@PathVariable id: UUID): NotificationDto {
        val currentUser = currentUserResolver.current()
        return notificationService.markRead(currentUser.userId, id)
    }
}
