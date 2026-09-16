package com.aj2.aj2.modules.document.api

import com.aj2.aj2.modules.document.application.DocumentService
import com.aj2.aj2.modules.document.application.dto.DocumentRequestDto
import com.aj2.aj2.modules.document.application.dto.DocumentSubmissionDto
import com.aj2.aj2.modules.document.application.dto.SubmitStandaloneRequest
import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/documents")
@PreAuthorize("isAuthenticated()")
class DocumentController(
    private val documentService: DocumentService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @PostMapping("/submissions", consumes = ["multipart/form-data"])
    fun submitStandalone(@ModelAttribute request: SubmitStandaloneRequest): DocumentSubmissionDto {
        val currentUser = currentUserResolver.current()
        return documentService.submitStandalone(currentUser.userId, request)
    }

    @PostMapping("/requests/{requestId}/submissions", consumes = ["multipart/form-data"])
    fun submitAgainstRequest(
        @PathVariable requestId: UUID,
        @RequestPart("file") file: MultipartFile,
    ): DocumentSubmissionDto {
        val currentUser = currentUserResolver.current()
        return documentService.submitAgainstRequest(currentUser.userId, requestId, file)
    }

    @GetMapping("/required")
    fun required(): List<DocumentRequestDto> {
        val currentUser = currentUserResolver.current()
        return documentService.listRequired(currentUser.userId)
    }

    @GetMapping("/submissions")
    fun submissions(): List<DocumentSubmissionDto> {
        val currentUser = currentUserResolver.current()
        return documentService.listSubmissions(currentUser.userId)
    }
}
