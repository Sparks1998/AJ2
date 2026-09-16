package com.aj2.aj2.modules.workshop.domain

import java.time.Instant
import java.util.UUID

interface WorkshopRepository {
    fun findById(id: UUID): Workshop?
    fun findAll(): List<Workshop>
    fun findByStartsAtAfter(now: Instant): List<Workshop>
    fun save(workshop: Workshop): Workshop
}
