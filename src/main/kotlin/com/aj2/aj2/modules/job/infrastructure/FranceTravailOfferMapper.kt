package com.aj2.aj2.modules.job.infrastructure

import com.aj2.aj2.modules.job.application.dto.JobOfferDto
import com.aj2.aj2.modules.job.domain.JobSource
import tools.jackson.databind.JsonNode

/**
 * Maps France Travail's raw "Offres d'emploi v2" JSON into our unified
 * JobOfferDto. Field names (resultats, id, intitule, entreprise.nom,
 * lieuTravail.libelle, typeContrat, origineOffre.urlOrigine, dateCreation)
 * are informed by community references, not independently verified against
 * the official spec - revisit once a real response has been inspected.
 */
object FranceTravailOfferMapper {
    fun toJobOfferDtos(searchResponse: JsonNode): List<JobOfferDto> {
        val offers = mutableListOf<JobOfferDto>()
        for (offer in searchResponse.path("resultats")) {
            offers += toJobOfferDto(offer)
        }
        return offers
    }

    fun toJobOfferDto(offer: JsonNode): JobOfferDto = JobOfferDto(
        source = JobSource.FRANCE_TRAVAIL,
        sourceRef = offer.path("id").asString(null),
        title = offer.path("intitule").asString(null),
        company = offer.path("entreprise").path("nom").asString(null),
        location = offer.path("lieuTravail").path("libelle").asString(null),
        contractType = offer.path("typeContrat").asString(null),
        applicationUrl = offer.path("origineOffre").path("urlOrigine").asString(null),
        description = offer.path("description").asString(null),
        publishedAt = offer.path("dateCreation").asString(null),
    )
}
