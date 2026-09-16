package com.aj2.aj2.modules.admin.application

import com.aj2.aj2.modules.mission.application.dto.CreateMissionTemplateRequest
import com.aj2.aj2.modules.mission.application.dto.MissionTemplateDto
import com.aj2.aj2.modules.mission.application.dto.UpdateMissionTemplateRequest
import com.aj2.aj2.modules.mission.domain.MissionTemplate
import com.aj2.aj2.modules.mission.domain.MissionTemplateRepository
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MissionAdminService(
    private val missionTemplateRepository: MissionTemplateRepository,
) {
    @Transactional
    fun create(request: CreateMissionTemplateRequest): MissionTemplateDto {
        val saved = missionTemplateRepository.save(
            MissionTemplate(
                title = request.title,
                description = request.description,
                xpReward = request.xpReward,
                periodType = request.periodType,
                completionType = request.completionType,
                linkedAction = request.linkedAction,
                targetCount = request.targetCount,
                active = request.active ?: true,
            ),
        )
        return saved.toDto()
    }

    @Transactional
    fun update(id: UUID, request: UpdateMissionTemplateRequest): MissionTemplateDto {
        val missionTemplate = missionTemplateRepository.findById(id)
            ?: throw NotFoundException("Mission template $id not found")

        request.title?.let { missionTemplate.title = it }
        request.description?.let { missionTemplate.description = it }
        request.xpReward?.let { missionTemplate.xpReward = it }
        request.periodType?.let { missionTemplate.periodType = it }
        request.completionType?.let { missionTemplate.completionType = it }
        request.linkedAction?.let { missionTemplate.linkedAction = it }
        request.targetCount?.let { missionTemplate.targetCount = it }
        request.active?.let { missionTemplate.active = it }

        return missionTemplateRepository.save(missionTemplate).toDto()
    }
}

private fun MissionTemplate.toDto() = MissionTemplateDto(
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
