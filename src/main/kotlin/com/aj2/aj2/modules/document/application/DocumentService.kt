package com.aj2.aj2.modules.document.application

import com.aj2.aj2.modules.document.application.dto.DocumentRequestDto
import com.aj2.aj2.modules.document.application.dto.DocumentSubmissionDto
import com.aj2.aj2.modules.document.application.dto.SubmitStandaloneRequest
import com.aj2.aj2.modules.document.domain.DocumentRequest
import com.aj2.aj2.modules.document.domain.DocumentRequestRepository
import com.aj2.aj2.modules.document.domain.DocumentStatus
import com.aj2.aj2.modules.document.domain.DocumentSubmission
import com.aj2.aj2.modules.document.domain.DocumentSubmissionRepository
import com.aj2.aj2.modules.document.domain.DocumentTypeRepository
import com.aj2.aj2.modules.document.infrastructure.FileStorage
import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.mission.domain.MissionTemplateRepository
import com.aj2.aj2.shared.exceptions.BadRequestException
import com.aj2.aj2.shared.exceptions.ForbiddenException
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class DocumentService(
    private val documentRequestRepository: DocumentRequestRepository,
    private val documentSubmissionRepository: DocumentSubmissionRepository,
    private val documentTypeRepository: DocumentTypeRepository,
    private val missionTemplateRepository: MissionTemplateRepository,
    private val userRepository: UserRepository,
    private val fileStorage: FileStorage,
) {
    @Transactional
    fun submitStandalone(userId: UUID, request: SubmitStandaloneRequest): DocumentSubmissionDto {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")
        val documentType = documentTypeRepository.findById(request.documentTypeId)
            ?: throw NotFoundException("Document type ${request.documentTypeId} not found")
        val documentMission = request.documentMissionId?.let {
            missionTemplateRepository.findById(it) ?: throw NotFoundException("Mission template $it not found")
        }

        val stored = fileStorage.store(request.file)

        val submission = documentSubmissionRepository.save(
            DocumentSubmission(
                documentRequest = null,
                documentMission = documentMission,
                documentType = documentType,
                submittedBy = user,
                fileKey = stored.fileKey,
                fileName = stored.fileName,
                mimeType = stored.mimeType,
                sizeBytes = stored.sizeBytes,
                version = 1,
            ),
        )

        return submission.toDto()
    }

    @Transactional
    fun submitAgainstRequest(userId: UUID, requestId: UUID, file: MultipartFile): DocumentSubmissionDto {
        val documentRequest = documentRequestRepository.findById(requestId)
            ?: throw NotFoundException("Document request $requestId not found")

        if (documentRequest.user.id != userId) {
            throw ForbiddenException("This document request does not belong to the caller")
        }
        if (documentRequest.status != DocumentStatus.REQUESTED && documentRequest.status != DocumentStatus.NEEDS_REVISION) {
            throw BadRequestException("Document request $requestId is not open for submission")
        }

        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")
        val stored = fileStorage.store(file)
        val nextVersion = documentSubmissionRepository.countByDocumentRequestId(requestId).toInt() + 1

        val submission = documentSubmissionRepository.save(
            DocumentSubmission(
                documentRequest = documentRequest,
                documentMission = null,
                documentType = documentRequest.documentType,
                submittedBy = user,
                fileKey = stored.fileKey,
                fileName = stored.fileName,
                mimeType = stored.mimeType,
                sizeBytes = stored.sizeBytes,
                version = nextVersion,
            ),
        )

        documentRequest.status = DocumentStatus.SUBMITTED
        documentRequestRepository.save(documentRequest)

        return submission.toDto()
    }

    fun listRequired(userId: UUID): List<DocumentRequestDto> =
        documentRequestRepository.findByUserIdAndStatusIn(
            userId,
            listOf(DocumentStatus.REQUESTED, DocumentStatus.NEEDS_REVISION),
        ).map { it.toDto() }

    fun listSubmissions(userId: UUID): List<DocumentSubmissionDto> =
        documentSubmissionRepository.findBySubmittedById(userId).map { it.toDto() }
}

internal fun DocumentRequest.toDto() = DocumentRequestDto(
    id = id!!,
    userId = user.id!!,
    documentTypeId = documentType.id!!,
    requestedBy = requestedBy.id!!,
    documentMissionId = documentMission?.id,
    status = status,
    note = note,
    requestedAt = requestedAt,
    dueDate = dueDate,
)

internal fun DocumentSubmission.toDto() = DocumentSubmissionDto(
    id = id!!,
    documentRequestId = documentRequest?.id,
    documentMissionId = documentMission?.id,
    documentTypeId = documentType.id!!,
    submittedBy = submittedBy.id!!,
    fileName = fileName,
    mimeType = mimeType,
    sizeBytes = sizeBytes,
    version = version,
    submittedAt = submittedAt,
    reviewedBy = reviewedBy?.id,
    reviewedAt = reviewedAt,
    reviewComment = reviewComment,
    decision = decision,
)
