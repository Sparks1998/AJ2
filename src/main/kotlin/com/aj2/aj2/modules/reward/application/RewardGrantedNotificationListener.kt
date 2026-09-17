package com.aj2.aj2.modules.reward.application

import com.aj2.aj2.modules.notification.application.NotificationService
import com.aj2.aj2.modules.reward.domain.events.RewardGrantedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * First side effect of an auto-granted reward: a notification row.
 * Additional side effects can be added as their own listeners for
 * RewardGrantedEvent without touching RewardGrantService.
 */
@Component
class RewardGrantedNotificationListener(
    private val notificationService: NotificationService,
) {
    @EventListener
    fun onRewardGranted(event: RewardGrantedEvent) {
        notificationService.notify(
            userId = event.userId,
            type = "REWARD_GRANTED",
            title = "Nouvelle récompense !",
            body = "Vous avez débloqué une nouvelle récompense : ${event.rewardTitle}",
            titleLocKey = "notification.reward_granted.title",
            bodyLocKey = "notification.reward_granted.body",
            bodyLocArgs = listOf(event.rewardTitle),
        )
    }
}
