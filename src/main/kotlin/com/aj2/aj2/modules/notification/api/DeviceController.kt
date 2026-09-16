package com.aj2.aj2.modules.notification.api

import com.aj2.aj2.modules.identity.application.CurrentUserResolver
import com.aj2.aj2.modules.notification.application.DeviceService
import com.aj2.aj2.modules.notification.application.dto.RegisterDeviceRequest
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/devices")
@PreAuthorize("isAuthenticated()")
class DeviceController(
    private val deviceService: DeviceService,
    private val currentUserResolver: CurrentUserResolver,
) {
    @PostMapping
    fun register(@Valid @RequestBody request: RegisterDeviceRequest) {
        val currentUser = currentUserResolver.current()
        deviceService.register(currentUser.userId, request)
    }
}
