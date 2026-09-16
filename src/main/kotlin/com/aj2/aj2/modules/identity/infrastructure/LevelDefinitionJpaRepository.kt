package com.aj2.aj2.modules.identity.infrastructure

import com.aj2.aj2.modules.identity.domain.LevelDefinition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LevelDefinitionJpaRepository : JpaRepository<LevelDefinition, Int> {
    @Query(
        """
        select ld from LevelDefinition ld
        where ld.level = (
            select max(ld2.level) from LevelDefinition ld2 where ld2.xpRequired <= :xpTotal
        )
        """,
    )
    fun findHighestLevelForXp(@Param("xpTotal") xpTotal: Int): LevelDefinition?
}
