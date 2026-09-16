package com.aj2.aj2.modules.job.domain

import com.aj2.aj2.modules.identity.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "job_applications")
class JobApplication(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "source", nullable = false)
    var source: String,

    @Column(name = "source_ref", nullable = false)
    var sourceRef: String,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "company")
    var company: String? = null,

    @Column(name = "application_url", nullable = false)
    var applicationUrl: String,

    @Column(name = "opened_at", nullable = false)
    var openedAt: Instant = Instant.now(),
)
