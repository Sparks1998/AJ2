package com.aj2.aj2.modules.job.application.dto

import com.aj2.aj2.modules.job.domain.JobSource

data class JobOfferDto(
    val source: JobSource,
    val sourceRef: String?,
    val title: String?,
    val company: String?,
    val location: String?,
    val contractType: String?,
    val applicationUrl: String?,
    val description: String?,
    val publishedAt: String?,
)
