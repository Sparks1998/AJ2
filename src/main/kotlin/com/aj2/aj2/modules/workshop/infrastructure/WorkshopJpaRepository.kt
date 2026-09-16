package com.aj2.aj2.modules.workshop.infrastructure

import com.aj2.aj2.modules.workshop.domain.Workshop
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.UUID

interface WorkshopJpaRepository : JpaRepository<Workshop, UUID> {
    fun findByStartsAtAfter(now: Instant): List<Workshop>
}
