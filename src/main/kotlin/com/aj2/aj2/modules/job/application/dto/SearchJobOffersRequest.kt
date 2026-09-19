package com.aj2.aj2.modules.job.application.dto

import com.aj2.aj2.modules.job.domain.JobSource

data class SearchJobOffersRequest(
    /** If null, results are combined across every supported website. If set, only that website is queried. */
    val website: JobSource? = null,
    /** Free-text keywords / job title (motsCles). */
    val keywords: String? = null,
    /** 5-digit INSEE commune code, e.g. 75101 for Paris 1st (commune). */
    val commune: String? = null,
    /** Department code, e.g. "75" (departement). */
    val departement: String? = null,
    /** Search radius in km - only effective when commune is set (rayon). */
    val distanceKm: Int? = null,
    /** CDI, CDD, MIS, SAI (typeContrat). */
    val contractType: String? = null,
    /** Experience level code (experience). */
    val experienceLevel: String? = null,
    /** Qualification level code (qualification). */
    val qualification: String? = null,
    /** Minimum salary (salaireMin). */
    val minSalary: Int? = null,
    val page: Int = 0,
    val pageSize: Int = 20,
)
