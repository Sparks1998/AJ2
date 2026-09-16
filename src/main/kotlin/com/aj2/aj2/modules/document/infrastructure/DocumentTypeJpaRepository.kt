package com.aj2.aj2.modules.document.infrastructure

import com.aj2.aj2.modules.document.domain.DocumentType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DocumentTypeJpaRepository : JpaRepository<DocumentType, UUID>
