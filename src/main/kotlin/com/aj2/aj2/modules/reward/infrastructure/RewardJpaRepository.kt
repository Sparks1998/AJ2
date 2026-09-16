package com.aj2.aj2.modules.reward.infrastructure

import com.aj2.aj2.modules.reward.domain.Reward
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RewardJpaRepository : JpaRepository<Reward, UUID>
