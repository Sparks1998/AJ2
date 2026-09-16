package com.aj2.aj2.modules.workshop.domain

import java.util.UUID

interface WorkshopRegistrationRepository {
    fun findByUserId(userId: UUID): List<WorkshopRegistration>
    fun findByWorkshopIdAndUserId(workshopId: UUID, userId: UUID): WorkshopRegistration?
    fun save(workshopRegistration: WorkshopRegistration): WorkshopRegistration
}
