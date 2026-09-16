package com.aj2.aj2.modules.mission.domain

import com.aj2.aj2.modules.identity.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "mission_completions")
class MissionCompletion(
    @Id
    @Column(name = "id")
    var id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_template_id", nullable = false)
    var missionTemplate: MissionTemplate,

    @Column(name = "xp_awarded", nullable = false)
    var xpAwarded: Int,

    @Column(name = "completed_at", nullable = false)
    var completedAt: Instant,

    @Column(name = "synced_at", nullable = false)
    var syncedAt: Instant = Instant.now(),
)
