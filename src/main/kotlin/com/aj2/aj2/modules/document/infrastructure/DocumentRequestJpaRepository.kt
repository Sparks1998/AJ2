package com.aj2.aj2.modules.document.infrastructure

import com.aj2.aj2.modules.document.domain.DocumentRequest
import com.aj2.aj2.modules.document.domain.DocumentStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DocumentRequestJpaRepository : JpaRepository<DocumentRequest, UUID> {
    fun findByUser_IdAndStatusIn(userId: UUID, statuses: List<DocumentStatus>): List<DocumentRequest>
}
