package com.aj2.aj2.modules.mission.infrastructure

import com.aj2.aj2.modules.mission.domain.MissionPeriodType
import com.aj2.aj2.modules.mission.domain.MissionTemplate
import com.aj2.aj2.modules.mission.domain.MissionTemplateRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MissionTemplateRepositoryImpl(
    private val jpaRepository: MissionTemplateJpaRepository,
) : MissionTemplateRepository {
    override fun findById(id: UUID): MissionTemplate? = jpaRepository.findById(id).orElse(null)

    override fun findByPeriodType(periodType: MissionPeriodType): List<MissionTemplate> =
        jpaRepository.findByPeriodType(periodType)

    override fun findAll(): List<MissionTemplate> = jpaRepository.findAll()

    override fun save(missionTemplate: MissionTemplate): MissionTemplate = jpaRepository.save(missionTemplate)
}
