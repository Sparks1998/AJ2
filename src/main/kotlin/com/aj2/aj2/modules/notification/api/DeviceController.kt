package com.aj2.aj2.modules.notification.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.notification.application.DeviceService
import com.aj2.aj2.modules.notification.application.dto.RegisterDeviceRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Devices", description = "FCM device token registration for push notifications")
@RestController
@RequestMapping("/devices")
class DeviceController(
    private val deviceService: DeviceService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @Operation(
        summary = "Register or refresh this device's FCM token for the current session",
        description = "A given fcmToken always maps to exactly one row - re-registering the same token " +
            "revokes whatever session it previously belonged to.",
    )
    @PostMapping
    fun register(@Valid @RequestBody request: RegisterDeviceRequest, authentication: Authentication) {
        val currentUser = currentUserResolver.current()
        val jwtAuth = authentication as JwtAuthenticationToken
        val authTokenId = UUID.fromString(jwtAuth.token.id)
        deviceService.register(currentUser.userId, request, authTokenId)
    }
}
