package com.aj2.aj2.modules.identity.application.dto

import com.aj2.aj2.modules.identity.domain.Gender
import com.aj2.aj2.modules.identity.domain.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateUserAdminRequest(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotNull
    val role: UserRole,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: Gender? = null,
    val phoneNumber: String? = null,
    val dateOfBirth: LocalDate? = null,
)
