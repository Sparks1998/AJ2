package com.aj2.aj2.modules.job.api

import com.aj2.aj2.modules.job.application.JobSearchService
import com.aj2.aj2.modules.job.application.dto.JobOfferDto
import com.aj2.aj2.modules.job.application.dto.SearchJobOffersRequest
import com.aj2.aj2.modules.job.domain.JobSource
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tools.jackson.databind.JsonNode

@Tag(name = "Job Search", description = "Search live job offers across supported websites")
@RestController
@RequestMapping("/search")
class JobSearchController(
    private val jobSearchService: JobSearchService,
) {
    @Operation(
        summary = "Search job offers",
        description = "If website is omitted, results are combined across every supported website " +
            "(pageSize split evenly between them). If set, only that website is queried using the full pageSize.",
    )
    @GetMapping
    fun search(
        @Parameter(description = "Restrict results to one website; omit to combine all supported websites")
        @RequestParam(required = false)
        website: JobSource?,
        @Parameter(description = "Free-text keywords / job title")
        @RequestParam(required = false)
        keywords: String?,
        @Parameter(description = "5-digit INSEE commune code, e.g. 75101 for Paris 1st")
        @RequestParam(required = false)
        commune: String?,
        @Parameter(description = "Department code, e.g. 75")
        @RequestParam(required = false)
        departement: String?,
        @Parameter(description = "Search radius in km - only effective when commune is set")
        @RequestParam(required = false)
        distanceKm: Int?,
        @Parameter(description = "Contract type: CDI, CDD, MIS, SAI")
        @RequestParam(required = false)
        contractType: String?,
        @Parameter(description = "Experience level code")
        @RequestParam(required = false)
        experienceLevel: String?,
        @Parameter(description = "Qualification level code")
        @RequestParam(required = false)
        qualification: String?,
        @Parameter(description = "Minimum salary")
        @RequestParam(required = false)
        minSalary: Int?,
        @Parameter(description = "Zero-based page index")
        @RequestParam(defaultValue = "0")
        page: Int,
        @Parameter(description = "Results per page (France Travail caps this at 150)")
        @RequestParam(defaultValue = "20")
        pageSize: Int,
    ): List<JobOfferDto> = jobSearchService.search(
        SearchJobOffersRequest(
            website = website,
            keywords = keywords,
            commune = commune,
            departement = departement,
            distanceKm = distanceKm,
            contractType = contractType,
            experienceLevel = experienceLevel,
            qualification = qualification,
            minSalary = minSalary,
            page = page,
            pageSize = pageSize,
        ),
    )

    @Operation(summary = "Get a single job offer's details from France Travail")
    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): JsonNode = jobSearchService.getById(id)
}
