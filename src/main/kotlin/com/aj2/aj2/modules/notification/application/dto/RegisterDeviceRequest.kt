package com.aj2.aj2.modules.notification.application.dto

import com.aj2.aj2.modules.notification.domain.DevicePlatform
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegisterDeviceRequest(
    @field:NotBlank
    val fcmToken: String,
    @field:NotNull
    var platform: DevicePlatform,
)
