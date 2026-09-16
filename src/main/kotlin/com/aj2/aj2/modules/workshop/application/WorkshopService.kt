package com.aj2.aj2.modules.workshop.application

import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.workshop.application.dto.WorkshopDto
import com.aj2.aj2.modules.workshop.application.dto.WorkshopRegistrationDto
import com.aj2.aj2.modules.workshop.domain.Workshop
import com.aj2.aj2.modules.workshop.domain.WorkshopRegistration
import com.aj2.aj2.modules.workshop.domain.WorkshopRegistrationRepository
import com.aj2.aj2.modules.workshop.domain.WorkshopRepository
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class WorkshopService(
    private val workshopRepository: WorkshopRepository,
    private val workshopRegistrationRepository: WorkshopRegistrationRepository,
    private val userRepository: UserRepository,
) {
    fun list(upcoming: Boolean): List<WorkshopDto> =
        (if (upcoming) workshopRepository.findByStartsAtAfter(Instant.now()) else workshopRepository.findAll())
            .map { it.toDto() }

    @Transactional
    fun register(userId: UUID, workshopId: UUID): WorkshopRegistrationDto {
        val workshop = workshopRepository.findById(workshopId)
            ?: throw NotFoundException("Workshop $workshopId not found")
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")

        val saved = workshopRegistrationRepository.save(WorkshopRegistration(workshop = workshop, user = user))
        return saved.toDto()
    }

    fun listRegistered(userId: UUID): List<WorkshopRegistrationDto> =
        workshopRegistrationRepository.findByUserId(userId).map { it.toDto() }
}

internal fun Workshop.toDto() = WorkshopDto(
    id = id!!,
    title = title,
    description = description,
    location = location,
    startsAt = startsAt,
    endsAt = endsAt,
    createdBy = createdBy.id!!,
)

internal fun WorkshopRegistration.toDto() = WorkshopRegistrationDto(
    id = id!!,
    workshopId = workshop.id!!,
    userId = user.id!!,
    registeredAt = registeredAt,
    attendedAt = attendedAt,
)
