package com.aj2.aj2.modules.admin.api

import com.aj2.aj2.modules.admin.application.UserAdminService
import com.aj2.aj2.modules.identity.application.dto.CreateUserAdminRequest
import com.aj2.aj2.modules.identity.application.dto.UserDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Admin - Users", description = "Admin-only user account management")
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
class UserAdminController(
    private val userAdminService: UserAdminService,
) {
    @Operation(
        summary = "Register a new user",
        description = "Creates a user account. If password is omitted, dateOfBirth is required and the " +
            "default password is the date of birth formatted as ddMMyyyy (e.g. 21/02/1998 -> \"21021998\").",
    )
    @PostMapping
    fun create(@Valid @RequestBody request: CreateUserAdminRequest): UserDto = userAdminService.create(request)
}
