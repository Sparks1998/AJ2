package com.aj2.aj2.modules.identity.application

import com.aj2.aj2.modules.identity.domain.events.LevelUpEvent
import com.aj2.aj2.modules.notification.application.NotificationService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * First side effect of a level-up: a notification row. Additional side
 * effects (badges, etc.) can be added as their own listeners for
 * LevelUpEvent without touching UserXpService.awardXp.
 */
@Component
class LevelUpNotificationListener(
    private val notificationService: NotificationService,
) {
    @EventListener
    fun onLevelUp(event: LevelUpEvent) {
        notificationService.notify(
            userId = event.userId,
            type = "LEVEL_UP",
            title = "Level up!",
            body = "You reached level ${event.newLevel}: ${event.newLevelTitle}",
        )
    }
}
