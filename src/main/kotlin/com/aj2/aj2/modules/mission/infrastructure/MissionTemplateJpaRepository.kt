package com.aj2.aj2.modules.mission.infrastructure

import com.aj2.aj2.modules.mission.domain.MissionPeriodType
import com.aj2.aj2.modules.mission.domain.MissionTemplate
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MissionTemplateJpaRepository : JpaRepository<MissionTemplate, UUID> {
    fun findByPeriodType(periodType: MissionPeriodType): List<MissionTemplate>
}
