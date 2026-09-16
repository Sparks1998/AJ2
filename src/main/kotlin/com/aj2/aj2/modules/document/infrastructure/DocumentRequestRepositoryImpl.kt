package com.aj2.aj2.modules.document.infrastructure

import com.aj2.aj2.modules.document.domain.DocumentRequest
import com.aj2.aj2.modules.document.domain.DocumentRequestRepository
import com.aj2.aj2.modules.document.domain.DocumentStatus
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class DocumentRequestRepositoryImpl(
    private val jpaRepository: DocumentRequestJpaRepository,
) : DocumentRequestRepository {
    override fun findById(id: UUID): DocumentRequest? = jpaRepository.findById(id).orElse(null)

    override fun findByUserIdAndStatusIn(userId: UUID, statuses: List<DocumentStatus>): List<DocumentRequest> =
        jpaRepository.findByUser_IdAndStatusIn(userId, statuses)

    override fun save(documentRequest: DocumentRequest): DocumentRequest = jpaRepository.save(documentRequest)
}
