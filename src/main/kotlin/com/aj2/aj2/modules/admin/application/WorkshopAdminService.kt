package com.aj2.aj2.modules.admin.application

import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.workshop.application.dto.CreateWorkshopRequest
import com.aj2.aj2.modules.workshop.application.dto.UpdateWorkshopRequest
import com.aj2.aj2.modules.workshop.application.dto.WorkshopDto
import com.aj2.aj2.modules.workshop.application.dto.WorkshopRegistrationDto
import com.aj2.aj2.modules.workshop.domain.Workshop
import com.aj2.aj2.modules.workshop.domain.WorkshopRegistrationRepository
import com.aj2.aj2.modules.workshop.domain.WorkshopRepository
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class WorkshopAdminService(
    private val workshopRepository: WorkshopRepository,
    private val workshopRegistrationRepository: WorkshopRegistrationRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun create(adminUserId: UUID, request: CreateWorkshopRequest): WorkshopDto {
        val admin = userRepository.findById(adminUserId) ?: throw NotFoundException("User $adminUserId not found")

        val saved = workshopRepository.save(
            Workshop(
                title = request.title,
                description = request.description,
                location = request.location,
                startsAt = request.startsAt,
                endsAt = request.endsAt,
                createdBy = admin,
            ),
        )
        return saved.toDto()
    }

    @Transactional
    fun update(id: UUID, request: UpdateWorkshopRequest): WorkshopDto {
        val workshop = workshopRepository.findById(id) ?: throw NotFoundException("Workshop $id not found")

        request.title?.let { workshop.title = it }
        request.description?.let { workshop.description = it }
        request.location?.let { workshop.location = it }
        request.startsAt?.let { workshop.startsAt = it }
        request.endsAt?.let { workshop.endsAt = it }

        return workshopRepository.save(workshop).toDto()
    }

    @Transactional
    fun markAttended(workshopId: UUID, userId: UUID): WorkshopRegistrationDto {
        val registration = workshopRegistrationRepository.findByWorkshopIdAndUserId(workshopId, userId)
            ?: throw NotFoundException("Registration for workshop $workshopId and user $userId not found")

        registration.attendedAt = Instant.now()
        return workshopRegistrationRepository.save(registration).toDto()
    }
}

private fun Workshop.toDto() = WorkshopDto(
    id = id!!,
    title = title,
    description = description,
    location = location,
    startsAt = startsAt,
    endsAt = endsAt,
    createdBy = createdBy.id!!,
)

private fun com.aj2.aj2.modules.workshop.domain.WorkshopRegistration.toDto() = WorkshopRegistrationDto(
    id = id!!,
    workshopId = workshop.id!!,
    userId = user.id!!,
    registeredAt = registeredAt,
    attendedAt = attendedAt,
)
