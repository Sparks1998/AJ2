package com.aj2.aj2.modules.notification.application

import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.notification.application.dto.RegisterDeviceRequest
import com.aj2.aj2.modules.notification.domain.DeviceToken
import com.aj2.aj2.modules.notification.domain.DeviceTokenRepository
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeviceService(
    private val deviceTokenRepository: DeviceTokenRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun register(userId: UUID, request: RegisterDeviceRequest) {
        val user = userRepository.findById(userId) ?: throw NotFoundException("User $userId not found")
        deviceTokenRepository.save(
            DeviceToken(user = user, fcmToken = request.fcmToken, platform = request.platform),
        )
    }
}
