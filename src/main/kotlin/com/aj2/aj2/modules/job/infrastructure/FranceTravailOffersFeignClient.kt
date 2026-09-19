package com.aj2.aj2.modules.job.infrastructure

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import tools.jackson.databind.JsonNode

@FeignClient(name = "france-travail-offers", url = $$"${france-travail.offers-url}")
interface FranceTravailOffersFeignClient {
    @GetMapping("/search")
    fun search(
        @RequestHeader("Authorization") authorization: String,
        @RequestParam("motsCles") keywords: String?,
        @RequestParam("commune") commune: String?,
        @RequestParam("departement") departement: String?,
        @RequestParam("rayon") distanceKm: Int?,
        @RequestParam("typeContrat") contractType: String?,
        @RequestParam("experience") experienceLevel: String?,
        @RequestParam("qualification") qualification: String?,
        @RequestParam("salaireMin") minSalary: Int?,
        @RequestParam("range") range: String,
    ): JsonNode

    @GetMapping("/{id}")
    fun getById(
        @RequestHeader("Authorization") authorization: String,
        @PathVariable("id") id: String,
    ): JsonNode
}
