package com.aj2.aj2.modules.job.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.job.application.JobApplicationService
import com.aj2.aj2.modules.job.application.dto.CreateJobApplicationRequest
import com.aj2.aj2.modules.job.application.dto.JobApplicationDto
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/jobs/applications")
@PreAuthorize("isAuthenticated()")
class JobApplicationController(
    private val jobApplicationService: JobApplicationService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @PostMapping
    fun create(@Valid @RequestBody request: CreateJobApplicationRequest): JobApplicationDto {
        val currentUser = currentUserResolver.current()
        return jobApplicationService.create(currentUser.userId, request)
    }

    @GetMapping
    fun list(): List<JobApplicationDto> {
        val currentUser = currentUserResolver.current()
        return jobApplicationService.listForUser(currentUser.userId)
    }
}
