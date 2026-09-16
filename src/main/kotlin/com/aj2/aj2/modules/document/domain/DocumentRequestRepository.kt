package com.aj2.aj2.modules.document.domain

import java.util.UUID

interface DocumentRequestRepository {
    fun findById(id: UUID): DocumentRequest?
    fun findByUserIdAndStatusIn(userId: UUID, statuses: List<DocumentStatus>): List<DocumentRequest>
    fun save(documentRequest: DocumentRequest): DocumentRequest
}
