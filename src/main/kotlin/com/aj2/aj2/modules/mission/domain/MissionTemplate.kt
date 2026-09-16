package com.aj2.aj2.modules.mission.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@Table(name = "mission_templates")
class MissionTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "xp_reward", nullable = false)
    var xpReward: Int,

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "period_type", nullable = false, columnDefinition = "mission_period_type")
    var periodType: MissionPeriodType,

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "completion_type", nullable = false, columnDefinition = "mission_completion_type")
    var completionType: MissionCompletionType,

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "linked_action", columnDefinition = "mission_linked_action")
    var linkedAction: MissionLinkedAction? = null,

    @Column(name = "target_count")
    var targetCount: Int? = null,

    @Column(name = "active", nullable = false)
    var active: Boolean = true,
)
