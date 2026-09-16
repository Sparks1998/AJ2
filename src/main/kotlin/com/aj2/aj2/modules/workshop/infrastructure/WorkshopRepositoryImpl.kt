package com.aj2.aj2.modules.workshop.infrastructure

import com.aj2.aj2.modules.workshop.domain.Workshop
import com.aj2.aj2.modules.workshop.domain.WorkshopRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class WorkshopRepositoryImpl(
    private val jpaRepository: WorkshopJpaRepository,
) : WorkshopRepository {
    override fun findById(id: UUID): Workshop? = jpaRepository.findById(id).orElse(null)

    override fun findAll(): List<Workshop> = jpaRepository.findAll()

    override fun findByStartsAtAfter(now: Instant): List<Workshop> = jpaRepository.findByStartsAtAfter(now)

    override fun save(workshop: Workshop): Workshop = jpaRepository.save(workshop)
}
