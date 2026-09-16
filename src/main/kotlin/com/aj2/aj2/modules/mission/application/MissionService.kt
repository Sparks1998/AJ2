package com.aj2.aj2.modules.mission.application

import com.aj2.aj2.modules.identity.application.UserXpService
import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.mission.application.dto.CompleteMissionRequest
import com.aj2.aj2.modules.mission.application.dto.MissionCompletionDto
import com.aj2.aj2.modules.mission.application.dto.MissionTemplateDto
import com.aj2.aj2.modules.mission.domain.MissionCompletion
import com.aj2.aj2.modules.mission.domain.MissionCompletionRepository
import com.aj2.aj2.modules.mission.domain.MissionPeriodType
import com.aj2.aj2.modules.mission.domain.MissionTemplateRepository
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class MissionService(
    private val missionTemplateRepository: MissionTemplateRepository,
    private val missionCompletionRepository: MissionCompletionRepository,
    private val userRepository: UserRepository,
    private val userXpService: UserXpService,
) {
    fun listByPeriod(period: MissionPeriodType): List<MissionTemplateDto> =
        missionTemplateRepository.findByPeriodType(period).map { it.toDto() }

    @Transactional
    fun complete(userId: UUID, request: CompleteMissionRequest): MissionCompletionDto =
        recordCompletion(request.id, userId, request.missionTemplateId, request.completedAt)

    @Transactional
    fun completeForDocumentApproval(userId: UUID, missionTemplateId: UUID): MissionCompletionDto =
        recordCompletion(UUID.randomUUID(), userId, missionTemplateId, Instant.now())

    private fun recordCompletion(
        id: UUID,
        userId: UUID,
        missionTemplateId: UUID,
        completedAt: Instant,
    ): MissionCompletionDto {
        missionCompletionRepository.findById(id)?.let { return it.toDto() }

        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")
        val missionTemplate = missionTemplateRepository.findById(missionTemplateId)
            ?: throw NotFoundException("Mission template $missionTemplateId not found")

        val completion = missionCompletionRepository.save(
            MissionCompletion(
                id = id,
                user = user,
                missionTemplate = missionTemplate,
                xpAwarded = missionTemplate.xpReward,
                completedAt = completedAt,
            ),
        )

        userXpService.awardXp(userId, missionTemplate.xpReward)
        userXpService.updateStreak(userId)

        return completion.toDto()
    }
}

private fun com.aj2.aj2.modules.mission.domain.MissionTemplate.toDto() = MissionTemplateDto(
    id = id!!,
    title = title,
    description = description,
    xpReward = xpReward,
    periodType = periodType,
    completionType = completionType,
    linkedAction = linkedAction,
    targetCount = targetCount,
    active = active,
)

private fun MissionCompletion.toDto() = MissionCompletionDto(
    id = id,
    userId = user.id!!,
    missionTemplateId = missionTemplate.id!!,
    xpAwarded = xpAwarded,
    completedAt = completedAt,
    syncedAt = syncedAt,
)
