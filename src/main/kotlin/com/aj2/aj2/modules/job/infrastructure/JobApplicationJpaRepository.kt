package com.aj2.aj2.modules.job.infrastructure

import com.aj2.aj2.modules.job.domain.JobApplication
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JobApplicationJpaRepository : JpaRepository<JobApplication, UUID> {
    fun findByUser_Id(userId: UUID): List<JobApplication>
}
