package com.aj2.aj2.modules.job.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.job.application.JobApplicationService
import com.aj2.aj2.modules.job.application.dto.CreateJobApplicationRequest
import com.aj2.aj2.modules.job.application.dto.JobApplicationDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Job Applications", description = "Client job application tracking")
@RestController
@RequestMapping("/applications")
class JobApplicationController(
    private val jobApplicationService: JobApplicationService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @Operation(summary = "Record a new job application")
    @PostMapping
    fun create(@Valid @RequestBody request: CreateJobApplicationRequest): JobApplicationDto {
        val currentUser = currentUserResolver.current()
        return jobApplicationService.create(currentUser.userId, request)
    }

    @Operation(summary = "List the caller's job applications")
    @GetMapping
    fun list(): List<JobApplicationDto> {
        val currentUser = currentUserResolver.current()
        return jobApplicationService.listForUser(currentUser.userId)
    }
}
