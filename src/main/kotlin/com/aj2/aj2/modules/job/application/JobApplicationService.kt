package com.aj2.aj2.modules.job.application

import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.job.application.dto.CreateJobApplicationRequest
import com.aj2.aj2.modules.job.application.dto.JobApplicationDto
import com.aj2.aj2.modules.job.domain.JobApplication
import com.aj2.aj2.modules.job.domain.JobApplicationRepository
import com.aj2.aj2.modules.reward.infrastructure.aop.RewardTrigger
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class JobApplicationService(
    private val jobApplicationRepository: JobApplicationRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    @RewardTrigger("JOB_APPLICATION", "CURIOUS")
    fun create(userId: UUID, request: CreateJobApplicationRequest): JobApplicationDto {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")

        val saved = jobApplicationRepository.save(
            JobApplication(
                user = user,
                source = request.source,
                sourceRef = request.sourceRef,
                title = request.title,
                company = request.company,
                applicationUrl = request.applicationUrl,
            ),
        )

        return saved.toDto()
    }

    fun listForUser(userId: UUID): List<JobApplicationDto> =
        jobApplicationRepository.findByUserId(userId).map { it.toDto() }
}

private fun JobApplication.toDto() = JobApplicationDto(
    id = id!!,
    userId = user.id!!,
    source = source,
    sourceRef = sourceRef,
    title = title,
    company = company,
    applicationUrl = applicationUrl,
    openedAt = openedAt,
)
