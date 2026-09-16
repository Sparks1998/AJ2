package com.aj2.aj2.modules.job.domain

import java.util.UUID

interface JobApplicationRepository {
    fun findByUserId(userId: UUID): List<JobApplication>
    fun save(jobApplication: JobApplication): JobApplication
}
