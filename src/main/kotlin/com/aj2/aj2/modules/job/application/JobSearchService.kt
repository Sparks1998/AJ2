package com.aj2.aj2.modules.job.application

import com.aj2.aj2.modules.job.application.dto.JobOfferDto
import com.aj2.aj2.modules.job.application.dto.SearchJobOffersRequest
import com.aj2.aj2.modules.job.domain.JobSource
import com.aj2.aj2.modules.job.infrastructure.FranceTravailJobClient
import com.aj2.aj2.modules.job.infrastructure.FranceTravailOfferMapper
import org.springframework.stereotype.Service
import tools.jackson.databind.JsonNode

@Service
class JobSearchService(
    private val franceTravailJobClient: FranceTravailJobClient,
) {
    private val numberOfSupportedWebsites = JobSource.entries.size

    fun search(request: SearchJobOffersRequest): List<JobOfferDto> {
        val perSitePageSize = if (request.website == null) {
            (request.pageSize / numberOfSupportedWebsites).coerceAtLeast(1)
        } else {
            request.pageSize
        }
        val effectiveRequest = request.copy(pageSize = perSitePageSize)

        val results = mutableListOf<JobOfferDto>()

        if (request.website == null || request.website == JobSource.FRANCE_TRAVAIL) {
            results += FranceTravailOfferMapper.toJobOfferDtos(franceTravailJobClient.search(effectiveRequest))
        }

        return results
    }

    fun getById(id: String): JsonNode = franceTravailJobClient.getById(id)
}
