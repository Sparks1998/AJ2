package com.aj2.aj2.modules.identity.application

import com.aj2.aj2.modules.identity.domain.LevelDefinitionRepository
import com.aj2.aj2.modules.identity.domain.User
import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.identity.domain.events.LevelUpEvent
import com.aj2.aj2.shared.exceptions.BadRequestException
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID

/**
 * Single shared entry point for every feature that grants or spends XP
 * (mission completion, document approval, reward flows, etc.) so the
 * xp_total/level bookkeeping is never duplicated per feature.
 */
@Service
class UserXpService(
    private val userRepository: UserRepository,
    private val levelDefinitionRepository: LevelDefinitionRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun awardXp(userId: UUID, amount: Int): User {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")
        user.xpTotal += amount

        val newLevelDefinition = levelDefinitionRepository.findHighestLevelForXp(user.xpTotal)
        if (newLevelDefinition != null && newLevelDefinition.level > user.level) {
            val oldLevel = user.level
            user.level = newLevelDefinition.level
            eventPublisher.publishEvent(
                LevelUpEvent(
                    userId = userId,
                    oldLevel = oldLevel,
                    newLevel = newLevelDefinition.level,
                    newLevelTitle = newLevelDefinition.title,
                ),
            )
        }

        return userRepository.save(user)
    }

    @Transactional
    fun deductXp(userId: UUID, amount: Int): User {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")
        if (user.xpTotal < amount) {
            throw BadRequestException("User $userId does not have enough XP")
        }
        user.xpTotal -= amount
        return userRepository.save(user)
    }

    /**
     * Called whenever a mission is completed. current_streak counts
     * consecutive calendar days (UTC) with at least one completed mission:
     *   - first-ever completion, or completion after a gap of 1+ full
     *     missed days -> streak resets to 1 (today starts a new streak)
     *   - completion on the day right after the last one -> streak + 1
     *   - a second completion on the same day as the last one -> unchanged
     * There is no background job: the reset is only ever written to the
     * DB reactively, on the next completion after the gap.
     */
    @Transactional
    fun updateStreak(userId: UUID) {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")
        val now = Instant.now()
        val today = now.atZone(ZoneOffset.UTC).toLocalDate()
        val lastActivityDate = user.lastActivityAt?.atZone(ZoneOffset.UTC)?.toLocalDate()

        user.currentStreak = when (lastActivityDate) {
            null -> 1
            today -> user.currentStreak
            today.minusDays(1) -> user.currentStreak + 1
            else -> 1
        }
        user.lastActivityAt = now

        userRepository.save(user)
    }
}
