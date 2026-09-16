package com.aj2.aj2.modules.mission.infrastructure

import com.aj2.aj2.modules.mission.domain.MissionCompletion
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MissionCompletionJpaRepository : JpaRepository<MissionCompletion, UUID>
