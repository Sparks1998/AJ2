package com.aj2.aj2.modules.reward.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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

    @Column(name = "xp_cost", nullable = false)
    var xpCost: Int,

    @Column(name = "stock")
    var stock: Int? = null,

    @Column(name = "active", nullable = false)
    var active: Boolean = true,
)
