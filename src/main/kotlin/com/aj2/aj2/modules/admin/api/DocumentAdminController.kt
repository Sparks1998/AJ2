package com.aj2.aj2.modules.admin.api

import com.aj2.aj2.modules.admin.application.DocumentAdminService
import com.aj2.aj2.modules.document.application.dto.CreateDocumentRequestRequest
import com.aj2.aj2.modules.document.application.dto.DocumentRequestDto
import com.aj2.aj2.modules.document.application.dto.DocumentSubmissionDto
import com.aj2.aj2.modules.document.application.dto.ReviewSubmissionRequest
import com.aj2.aj2.modules.document.domain.DocumentStatus
import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/documents")
@PreAuthorize("hasRole('ADMIN')")
class DocumentAdminController(
    private val documentAdminService: DocumentAdminService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @PostMapping("/clients/{userId}/requests")
    fun createRequest(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: CreateDocumentRequestRequest,
    ): DocumentRequestDto {
        val currentUser = currentUserResolver.current()
        return documentAdminService.createRequest(currentUser.userId, userId, request)
    }

    @PatchMapping("/submissions/{id}/review")
    fun review(
        @PathVariable id: UUID,
        @Valid @RequestBody request: ReviewSubmissionRequest,
    ): DocumentSubmissionDto {
        val currentUser = currentUserResolver.current()
        return documentAdminService.reviewSubmission(currentUser.userId, id, request)
    }

    @GetMapping("/submissions")
    fun queue(
        @RequestParam(required = false) status: DocumentStatus?,
        @RequestParam(required = false) clientId: UUID?,
    ): List<DocumentSubmissionDto> = documentAdminService.listQueue(status, clientId)
}
