package com.aj2.aj2.modules.job.infrastructure

import com.aj2.aj2.modules.job.domain.JobApplicationRepository
import com.aj2.aj2.modules.reward.application.RewardMetricProvider
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class JobApplicationsCountMetricProvider(
    private val jobApplicationRepository: JobApplicationRepository,
) : RewardMetricProvider {
    override val ruleCodes = listOf("JOB_APPLICATION", "CURIOUS")

    override fun currentValue(userId: UUID): Int = jobApplicationRepository.countByUserId(userId).toInt()
}
