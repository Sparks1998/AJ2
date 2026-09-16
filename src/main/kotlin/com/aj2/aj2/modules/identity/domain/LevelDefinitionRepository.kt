package com.aj2.aj2.modules.identity.domain

interface LevelDefinitionRepository {
    /** The highest level whose xp_required threshold is met by the given XP total. */
    fun findHighestLevelForXp(xpTotal: Int): LevelDefinition?
    fun findByLevel(level: Int): LevelDefinition?
    fun findAll(): List<LevelDefinition>
    fun save(levelDefinition: LevelDefinition): LevelDefinition
}
