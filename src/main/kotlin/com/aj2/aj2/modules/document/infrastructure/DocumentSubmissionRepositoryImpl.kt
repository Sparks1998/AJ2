package com.aj2.aj2.modules.document.infrastructure

import com.aj2.aj2.modules.document.domain.DocumentStatus
import com.aj2.aj2.modules.document.domain.DocumentSubmission
import com.aj2.aj2.modules.document.domain.DocumentSubmissionRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class DocumentSubmissionRepositoryImpl(
    private val jpaRepository: DocumentSubmissionJpaRepository,
) : DocumentSubmissionRepository {
    override fun findById(id: UUID): DocumentSubmission? = jpaRepository.findById(id).orElse(null)

    override fun findBySubmittedById(userId: UUID): List<DocumentSubmission> =
        jpaRepository.findBySubmittedBy_Id(userId)

    override fun countByDocumentRequestId(requestId: UUID): Long =
        jpaRepository.countByDocumentRequest_Id(requestId)

    override fun countUploadsByUserIdAndDocumentTypeCode(userId: UUID, documentTypeCode: String): Long =
        jpaRepository.countBySubmittedBy_IdAndDocumentType_CodeAndDocumentRequestIsNull(userId, documentTypeCode)

    override fun countUpdatesByUserIdAndDocumentTypeCode(userId: UUID, documentTypeCode: String): Long =
        jpaRepository.countBySubmittedBy_IdAndDocumentType_CodeAndDocumentRequestIsNotNull(userId, documentTypeCode)

    override fun search(status: DocumentStatus?, clientId: UUID?): List<DocumentSubmission> = when {
        status != null && clientId != null -> jpaRepository.findByDecisionAndSubmittedBy_Id(status, clientId)
        status != null -> jpaRepository.findByDecision(status)
        clientId != null -> jpaRepository.findBySubmittedBy_Id(clientId)
        else -> jpaRepository.findAll()
    }

    override fun save(documentSubmission: DocumentSubmission): DocumentSubmission =
        jpaRepository.save(documentSubmission)
}
