package com.aj2.aj2.modules.job.domain

import java.util.UUID

interface JobApplicationRepository {
    fun findByUserId(userId: UUID): List<JobApplication>
    fun countByUserId(userId: UUID): Long
    fun existsByUserIdAndSourceAndSourceRef(userId: UUID, source: JobSource, sourceRef: String): Boolean
    fun save(jobApplication: JobApplication): JobApplication
}
