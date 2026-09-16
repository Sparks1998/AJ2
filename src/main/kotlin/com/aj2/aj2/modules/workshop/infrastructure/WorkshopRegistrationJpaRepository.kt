package com.aj2.aj2.modules.workshop.infrastructure

import com.aj2.aj2.modules.workshop.domain.WorkshopRegistration
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface WorkshopRegistrationJpaRepository : JpaRepository<WorkshopRegistration, UUID> {
    fun findByUser_Id(userId: UUID): List<WorkshopRegistration>
    fun findByWorkshop_IdAndUser_Id(workshopId: UUID, userId: UUID): WorkshopRegistration?
}
