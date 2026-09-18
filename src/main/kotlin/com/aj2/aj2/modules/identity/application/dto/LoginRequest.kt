package com.aj2.aj2.modules.identity.application.dto

import com.aj2.aj2.modules.notification.domain.DevicePlatform
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LoginRequest(
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String,
    val rememberMe: Boolean? = false,
    @field:NotBlank
    @field:NotNull
    var fcmToken: String,
    @field:NotNull
    var platform: DevicePlatform,
)
