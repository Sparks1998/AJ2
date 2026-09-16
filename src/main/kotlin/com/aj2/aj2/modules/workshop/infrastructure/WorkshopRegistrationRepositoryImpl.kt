package com.aj2.aj2.modules.workshop.infrastructure

import com.aj2.aj2.modules.workshop.domain.WorkshopRegistration
import com.aj2.aj2.modules.workshop.domain.WorkshopRegistrationRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class WorkshopRegistrationRepositoryImpl(
    private val jpaRepository: WorkshopRegistrationJpaRepository,
) : WorkshopRegistrationRepository {
    override fun findByUserId(userId: UUID): List<WorkshopRegistration> = jpaRepository.findByUser_Id(userId)

    override fun findByWorkshopIdAndUserId(workshopId: UUID, userId: UUID): WorkshopRegistration? =
        jpaRepository.findByWorkshop_IdAndUser_Id(workshopId, userId)

    override fun save(workshopRegistration: WorkshopRegistration): WorkshopRegistration =
        jpaRepository.save(workshopRegistration)
}
