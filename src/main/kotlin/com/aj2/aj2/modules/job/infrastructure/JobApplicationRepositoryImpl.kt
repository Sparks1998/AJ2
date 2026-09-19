package com.aj2.aj2.modules.job.infrastructure

import com.aj2.aj2.modules.job.domain.JobApplication
import com.aj2.aj2.modules.job.domain.JobApplicationRepository
import com.aj2.aj2.modules.job.domain.JobSource
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JobApplicationRepositoryImpl(
    private val jpaRepository: JobApplicationJpaRepository,
) : JobApplicationRepository {
    override fun findByUserId(userId: UUID): List<JobApplication> = jpaRepository.findByUserId(userId)

    override fun countByUserId(userId: UUID): Long = jpaRepository.countByUserId(userId)

    override fun existsByUserIdAndSourceAndSourceRef(userId: UUID, source: JobSource, sourceRef: String): Boolean =
        jpaRepository.existsByUserIdAndSourceAndSourceRef(userId, source, sourceRef)

    override fun save(jobApplication: JobApplication): JobApplication = jpaRepository.save(jobApplication)
}
