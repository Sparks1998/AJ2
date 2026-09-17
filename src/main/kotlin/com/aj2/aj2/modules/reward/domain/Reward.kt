package com.aj2.aj2.modules.reward.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "rewards")
class Reward(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_rule_id", nullable = false)
    var rewardRule: RewardRule,

    @Column(name = "rule_threshold", nullable = false)
    var ruleThreshold: Int,

    @Column(name = "stock")
    var stock: Int? = null,

    @Column(name = "active", nullable = false)
    var active: Boolean = true,
)
