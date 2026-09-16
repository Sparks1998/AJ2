package com.aj2.aj2.modules.document.domain

import java.util.UUID

interface DocumentSubmissionRepository {
    fun findById(id: UUID): DocumentSubmission?
    fun findBySubmittedById(userId: UUID): List<DocumentSubmission>
    fun countByDocumentRequestId(requestId: UUID): Long
    fun search(status: DocumentStatus?, clientId: UUID?): List<DocumentSubmission>
    fun save(documentSubmission: DocumentSubmission): DocumentSubmission
}
