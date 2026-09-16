package com.aj2.aj2.modules.document.domain

import com.aj2.aj2.modules.identity.domain.User
import com.aj2.aj2.modules.mission.domain.MissionTemplate
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "document_submissions")
class DocumentSubmission(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_request_id")
    var documentRequest: DocumentRequest? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_mission_id")
    var documentMission: MissionTemplate? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = false)
    var documentType: DocumentType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by", nullable = false)
    var submittedBy: User,

    @Column(name = "file_key", nullable = false)
    var fileKey: String,

    @Column(name = "file_name", nullable = false)
    var fileName: String,

    @Column(name = "mime_type", nullable = false)
    var mimeType: String,

    @Column(name = "size_bytes", nullable = false)
    var sizeBytes: Long,

    @Column(name = "version", nullable = false)
    var version: Int = 1,

    @Column(name = "submitted_at", nullable = false)
    var submittedAt: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    var reviewedBy: User? = null,

    @Column(name = "reviewed_at")
    var reviewedAt: Instant? = null,

    @Column(name = "review_comment")
    var reviewComment: String? = null,

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "decision", columnDefinition = "document_status")
    var decision: DocumentStatus? = null,
)
