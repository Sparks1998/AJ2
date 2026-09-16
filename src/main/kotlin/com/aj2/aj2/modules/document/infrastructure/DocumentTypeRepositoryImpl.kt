package com.aj2.aj2.modules.document.infrastructure

import com.aj2.aj2.modules.document.domain.DocumentType
import com.aj2.aj2.modules.document.domain.DocumentTypeRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class DocumentTypeRepositoryImpl(
    private val jpaRepository: DocumentTypeJpaRepository,
) : DocumentTypeRepository {
    override fun findById(id: UUID): DocumentType? = jpaRepository.findById(id).orElse(null)
}
