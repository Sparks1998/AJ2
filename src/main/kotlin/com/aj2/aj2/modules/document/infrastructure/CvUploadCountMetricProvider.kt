package com.aj2.aj2.modules.document.infrastructure

import com.aj2.aj2.modules.document.domain.DocumentSubmissionRepository
import com.aj2.aj2.modules.document.domain.DocumentType
import com.aj2.aj2.modules.reward.application.RewardMetricProvider
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CvUploadCountMetricProvider(
    private val documentSubmissionRepository: DocumentSubmissionRepository,
) : RewardMetricProvider {
    override val ruleCodes = listOf("CV_UPLOAD")

    override fun currentValue(userId: UUID): Int =
        documentSubmissionRepository.countUploadsByUserIdAndDocumentTypeCode(userId, DocumentType.CV_CODE).toInt()
}
