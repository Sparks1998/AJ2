package com.aj2.aj2.modules.mission.infrastructure

import com.aj2.aj2.modules.mission.domain.MissionCompletion
import com.aj2.aj2.modules.mission.domain.MissionCompletionRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MissionCompletionRepositoryImpl(
    private val jpaRepository: MissionCompletionJpaRepository,
) : MissionCompletionRepository {
    override fun findById(id: UUID): MissionCompletion? = jpaRepository.findById(id).orElse(null)

    override fun save(missionCompletion: MissionCompletion): MissionCompletion = jpaRepository.save(missionCompletion)
}
