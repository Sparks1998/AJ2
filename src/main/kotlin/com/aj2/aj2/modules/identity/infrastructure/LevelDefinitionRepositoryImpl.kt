package com.aj2.aj2.modules.identity.infrastructure

import com.aj2.aj2.modules.identity.domain.LevelDefinition
import com.aj2.aj2.modules.identity.domain.LevelDefinitionRepository
import org.springframework.stereotype.Repository

@Repository
class LevelDefinitionRepositoryImpl(
    private val jpaRepository: LevelDefinitionJpaRepository,
) : LevelDefinitionRepository {
    override fun findHighestLevelForXp(xpTotal: Int): LevelDefinition? =
        jpaRepository.findHighestLevelForXp(xpTotal)

    override fun findByLevel(level: Int): LevelDefinition? = jpaRepository.findById(level).orElse(null)

    override fun findAll(): List<LevelDefinition> = jpaRepository.findAll().sortedBy { it.level }

    override fun save(levelDefinition: LevelDefinition): LevelDefinition = jpaRepository.save(levelDefinition)
}
