package com.aj2.aj2.modules.identity.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "level_definition")
class LevelDefinition(
    @Id
    @Column(name = "level")
    var level: Int,

    @Column(name = "xp_required", nullable = false)
    var xpRequired: Int,

    @Column(name = "title", nullable = false)
    var title: String,
)
