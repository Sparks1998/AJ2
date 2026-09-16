package com.aj2.aj2.modules.document.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "document_types")
class DocumentType(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null,

    @Column(name = "code", nullable = false, unique = true)
    var code: String,

    @Column(name = "label", nullable = false)
    var label: String,

    @Column(name = "active", nullable = false)
    var active: Boolean = true,
)
