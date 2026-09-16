package com.aj2.aj2.modules.notification.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.notification.application.NotificationService
import com.aj2.aj2.modules.notification.application.dto.NotificationDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/notifications")
@PreAuthorize("isAuthenticated()")
class NotificationController(
    private val notificationService: NotificationService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @GetMapping
    fun list(): List<NotificationDto> {
        val currentUser = currentUserResolver.current()
        return notificationService.listForUser(currentUser.userId)
    }

    @PatchMapping("/{id}/read")
    fun markRead(@PathVariable id: UUID): NotificationDto {
        val currentUser = currentUserResolver.current()
        return notificationService.markRead(currentUser.userId, id)
    }
}
