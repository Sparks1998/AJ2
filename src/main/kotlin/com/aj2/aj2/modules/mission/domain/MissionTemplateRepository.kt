package com.aj2.aj2.modules.mission.domain

import java.util.UUID

interface MissionTemplateRepository {
    fun findById(id: UUID): MissionTemplate?
    fun findByPeriodType(periodType: MissionPeriodType): List<MissionTemplate>
    fun findAll(): List<MissionTemplate>
    fun save(missionTemplate: MissionTemplate): MissionTemplate
}
