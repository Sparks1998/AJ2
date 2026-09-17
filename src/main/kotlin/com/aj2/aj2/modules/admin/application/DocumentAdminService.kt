package com.aj2.aj2.modules.admin.application

import com.aj2.aj2.modules.document.application.dto.CreateDocumentRequestRequest
import com.aj2.aj2.modules.document.application.dto.DocumentRequestDto
import com.aj2.aj2.modules.document.application.dto.DocumentSubmissionDto
import com.aj2.aj2.modules.document.application.dto.ReviewSubmissionRequest
import com.aj2.aj2.modules.document.domain.DocumentRequest
import com.aj2.aj2.modules.document.domain.DocumentRequestRepository
import com.aj2.aj2.modules.document.domain.DocumentStatus
import com.aj2.aj2.modules.document.domain.DocumentSubmissionRepository
import com.aj2.aj2.modules.document.domain.DocumentTypeRepository
import com.aj2.aj2.modules.document.application.toDto
import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.mission.application.MissionService
import com.aj2.aj2.modules.notification.application.NotificationService
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class DocumentAdminService(
    private val documentRequestRepository: DocumentRequestRepository,
    private val documentSubmissionRepository: DocumentSubmissionRepository,
    private val documentTypeRepository: DocumentTypeRepository,
    private val userRepository: UserRepository,
    private val notificationService: NotificationService,
    private val missionService: MissionService,
) {
    @Transactional
    fun createRequest(adminUserId: UUID, clientUserId: UUID, request: CreateDocumentRequestRequest): DocumentRequestDto {
        val client = userRepository.findById(clientUserId) ?: throw NotFoundException("User $clientUserId not found")
        val admin = userRepository.findById(adminUserId) ?: throw NotFoundException("User $adminUserId not found")
        val documentType = documentTypeRepository.findById(request.documentTypeId)
            ?: throw NotFoundException("Document type ${request.documentTypeId} not found")

        val documentRequest = documentRequestRepository.save(
            DocumentRequest(
                user = client,
                documentType = documentType,
                requestedBy = admin,
                status = DocumentStatus.REQUESTED,
                note = request.note,
                dueDate = request.dueDate,
            ),
        )

        notificationService.notify(
            userId = clientUserId,
            type = "DOCUMENT_REQUESTED",
            title = "Nouveau document demandé",
            body = documentType.label,
            titleLocKey = "notification.document_requested.title",
        )

        return documentRequest.toDto()
    }

    @Transactional
    fun reviewSubmission(adminUserId: UUID, submissionId: UUID, request: ReviewSubmissionRequest): DocumentSubmissionDto {
        val admin = userRepository.findById(adminUserId) ?: throw NotFoundException("User $adminUserId not found")
        val submission = documentSubmissionRepository.findById(submissionId)
            ?: throw NotFoundException("Document submission $submissionId not found")

        val wasAlreadyApproved = submission.decision == DocumentStatus.APPROVED

        submission.reviewedBy = admin
        submission.reviewedAt = Instant.now()
        submission.reviewComment = request.comment
        submission.decision = request.decision
        documentSubmissionRepository.save(submission)

        submission.documentRequest?.let { documentRequest ->
            documentRequest.status = request.decision
            documentRequestRepository.save(documentRequest)
        }

        if (!wasAlreadyApproved && request.decision == DocumentStatus.APPROVED) {
            submission.documentMission?.let { mission ->
                missionService.completeForDocumentApproval(submission.submittedBy.id!!, mission.id!!)
            }
        }

        return submission.toDto()
    }

    fun listQueue(status: DocumentStatus?, clientId: UUID?): List<DocumentSubmissionDto> =
        documentSubmissionRepository.search(status, clientId).map { it.toDto() }
}
