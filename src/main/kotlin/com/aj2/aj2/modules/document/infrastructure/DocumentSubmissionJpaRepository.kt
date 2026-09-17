package com.aj2.aj2.modules.document.infrastructure

import com.aj2.aj2.modules.document.domain.DocumentStatus
import com.aj2.aj2.modules.document.domain.DocumentSubmission
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DocumentSubmissionJpaRepository : JpaRepository<DocumentSubmission, UUID> {
    fun findBySubmittedBy_Id(userId: UUID): List<DocumentSubmission>
    fun countByDocumentRequest_Id(requestId: UUID): Long
    fun countBySubmittedBy_IdAndDocumentType_CodeAndDocumentRequestIsNull(userId: UUID, documentTypeCode: String): Long
    fun countBySubmittedBy_IdAndDocumentType_CodeAndDocumentRequestIsNotNull(userId: UUID, documentTypeCode: String): Long
    fun findByDecisionAndSubmittedBy_Id(decision: DocumentStatus, clientId: UUID): List<DocumentSubmission>
    fun findByDecision(decision: DocumentStatus): List<DocumentSubmission>
}
