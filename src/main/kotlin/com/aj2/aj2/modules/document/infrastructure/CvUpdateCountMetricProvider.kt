package com.aj2.aj2.modules.document.infrastructure

import com.aj2.aj2.modules.document.domain.DocumentSubmissionRepository
import com.aj2.aj2.modules.document.domain.DocumentType
import com.aj2.aj2.modules.reward.application.RewardMetricProvider
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CvUpdateCountMetricProvider(
    private val documentSubmissionRepository: DocumentSubmissionRepository,
) : RewardMetricProvider {
    override val ruleCodes = listOf("CV_UPDATE")

    override fun currentValue(userId: UUID): Int =
        documentSubmissionRepository.countUpdatesByUserIdAndDocumentTypeCode(userId, DocumentType.CV_CODE).toInt()
}
