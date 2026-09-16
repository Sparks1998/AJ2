package com.aj2.aj2.modules.document.domain

import java.util.UUID

interface DocumentTypeRepository {
    fun findById(id: UUID): DocumentType?
}
