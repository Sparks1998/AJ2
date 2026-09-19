package com.aj2.aj2.modules.job.domain

/**
 * The DB column (job_applications.source) is a plain VARCHAR on purpose -
 * unlike this project's other enums, it's not backed by a native Postgres
 * enum type, since adding a new source shouldn't require a schema
 * migration. This enum is the code-side source of truth for which values
 * are valid; new sources are added here.
 */
enum class JobSource {
    FRANCE_TRAVAIL,
    MAISON_EMPLOI,
}
