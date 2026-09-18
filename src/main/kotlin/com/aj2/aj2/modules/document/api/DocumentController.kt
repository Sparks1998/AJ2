package com.aj2.aj2.modules.document.api

import com.aj2.aj2.modules.document.application.DocumentService
import com.aj2.aj2.modules.document.application.dto.DocumentRequestDto
import com.aj2.aj2.modules.document.application.dto.DocumentSubmissionDto
import com.aj2.aj2.modules.document.application.dto.SubmitStandaloneRequest
import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Tag(name = "Documents", description = "Client document uploads and requirements")
@RestController
@RequestMapping("/documents")
class DocumentController(
    private val documentService: DocumentService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @Operation(summary = "Upload a document without an existing document request")
    @PostMapping("/submissions", consumes = ["multipart/form-data"])
    fun submitStandalone(@ModelAttribute request: SubmitStandaloneRequest): DocumentSubmissionDto {
        val currentUser = currentUserResolver.current()
        return documentService.submitStandalone(currentUser.userId, request)
    }

    @Operation(summary = "Upload a document in response to an admin-created document request")
    @PostMapping("/requests/{requestId}/submissions", consumes = ["multipart/form-data"])
    fun submitAgainstRequest(
        @PathVariable requestId: UUID,
        @RequestPart("file") file: MultipartFile,
    ): DocumentSubmissionDto {
        val currentUser = currentUserResolver.current()
        return documentService.submitAgainstRequest(currentUser.userId, requestId, file)
    }

    @Operation(summary = "List the caller's outstanding document requests")
    @GetMapping("/required")
    fun required(): List<DocumentRequestDto> {
        val currentUser = currentUserResolver.current()
        return documentService.listRequired(currentUser.userId)
    }

    @Operation(summary = "List the caller's document submissions")
    @GetMapping("/submissions")
    fun submissions(): List<DocumentSubmissionDto> {
        val currentUser = currentUserResolver.current()
        return documentService.listSubmissions(currentUser.userId)
    }
}
