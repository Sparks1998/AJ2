package com.aj2.aj2.modules.admin.application

import com.aj2.aj2.modules.identity.application.dto.CreateLevelDefinitionRequest
import com.aj2.aj2.modules.identity.application.dto.LevelDefinitionDto
import com.aj2.aj2.modules.identity.domain.LevelDefinition
import com.aj2.aj2.modules.identity.domain.LevelDefinitionRepository
import com.aj2.aj2.shared.exceptions.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LevelAdminService(
    private val levelDefinitionRepository: LevelDefinitionRepository,
) {
    fun list(): List<LevelDefinitionDto> = levelDefinitionRepository.findAll().map { it.toDto() }

    @Transactional
    fun create(request: CreateLevelDefinitionRequest): LevelDefinitionDto {
        if (levelDefinitionRepository.findByLevel(request.level) != null) {
            throw BadRequestException("Level ${request.level} already exists")
        }

        val saved = levelDefinitionRepository.save(
            LevelDefinition(
                level = request.level,
                xpRequired = request.xpRequired,
                title = request.title,
            ),
        )
        return saved.toDto()
    }
}

private fun LevelDefinition.toDto() = LevelDefinitionDto(
    level = level,
    xpRequired = xpRequired,
    title = title,
)
