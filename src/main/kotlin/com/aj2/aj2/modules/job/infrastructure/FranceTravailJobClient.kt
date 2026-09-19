package com.aj2.aj2.modules.job.infrastructure

import com.aj2.aj2.modules.job.application.dto.SearchJobOffersRequest
import org.springframework.stereotype.Component
import tools.jackson.databind.JsonNode

/**
 * Search parameter names (motsCles, commune, departement, rayon, typeContrat,
 * experience, qualification, salaireMin, range) are verified against the
 * France Travail "Offres d'emploi v2" API. The response is returned as raw
 * JSON rather than mapped into a typed DTO, since the exact response field
 * names weren't independently verifiable against the official spec - this
 * avoids guessing at a response contract.
 */
@Component
class FranceTravailJobClient(
    private val offersFeignClient: FranceTravailOffersFeignClient,
    private val tokenClient: FranceTravailTokenClient,
) {
    fun search(request: SearchJobOffersRequest): JsonNode {
        val rangeStart = request.page * request.pageSize
        val rangeEnd = rangeStart + request.pageSize - 1

        return offersFeignClient.search(
            authorization = "Bearer ${tokenClient.getAccessToken()}",
            keywords = request.keywords,
            commune = request.commune,
            departement = request.departement,
            distanceKm = request.distanceKm,
            contractType = request.contractType,
            experienceLevel = request.experienceLevel,
            qualification = request.qualification,
            minSalary = request.minSalary,
            range = "$rangeStart-$rangeEnd",
        )
    }

    fun getById(id: String): JsonNode =
        offersFeignClient.getById(authorization = "Bearer ${tokenClient.getAccessToken()}", id = id)
}
