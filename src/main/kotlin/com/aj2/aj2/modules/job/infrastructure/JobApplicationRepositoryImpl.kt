package com.aj2.aj2.modules.job.infrastructure

import com.aj2.aj2.modules.job.domain.JobApplication
import com.aj2.aj2.modules.job.domain.JobApplicationRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JobApplicationRepositoryImpl(
    private val jpaRepository: JobApplicationJpaRepository,
) : JobApplicationRepository {
    override fun findByUserId(userId: UUID): List<JobApplication> = jpaRepository.findByUser_Id(userId)

    override fun save(jobApplication: JobApplication): JobApplication = jpaRepository.save(jobApplication)
}
