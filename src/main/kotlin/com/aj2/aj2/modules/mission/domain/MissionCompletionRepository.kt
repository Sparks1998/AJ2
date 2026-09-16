package com.aj2.aj2.modules.mission.domain

import java.util.UUID

interface MissionCompletionRepository {
    fun findById(id: UUID): MissionCompletion?
    fun save(missionCompletion: MissionCompletion): MissionCompletion
}
