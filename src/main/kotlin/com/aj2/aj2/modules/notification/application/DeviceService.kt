package com.aj2.aj2.modules.notification.application

import com.aj2.aj2.modules.identity.domain.AuthTokenRepository
import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.modules.notification.application.dto.RegisterDeviceRequest
import com.aj2.aj2.modules.notification.domain.DevicePlatform
import com.aj2.aj2.modules.notification.domain.DeviceTokenRepository
import com.aj2.aj2.shared.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeviceService(
    private val deviceTokenRepository: DeviceTokenRepository,
    private val userRepository: UserRepository,
    private val authTokenRepository: AuthTokenRepository,
) {
    @Transactional
    fun register(userId: UUID, request: RegisterDeviceRequest, authTokenId: UUID) {
        register(userId, request.fcmToken, request.platform, authTokenId)
    }

    /**
     * A device (fcm token) belongs to exactly one record at a time, regardless
     * of which user it was last registered under - a user can be logged in on
     * several devices, but a single device is only ever one row. Upserting by
     * fcmToken (not by userId) is what lets a device correctly move to a
     * different user if it's later reused for another login. This has to be a
     * single atomic INSERT ... ON CONFLICT: a separate delete-then-insert pair
     * breaks under Hibernate's flush ordering (inserts always flush before
     * deletes within one flush, regardless of call order), which re-violates
     * the fcm_token unique constraint.
     */
    @Transactional
    fun register(userId: UUID, fcmToken: String, platform: DevicePlatform, authTokenId: UUID) {
        if (userRepository.findById(userId) == null) throw NotFoundException("User $userId not found")

        val previousAuthTokenId = deviceTokenRepository.findAuthTokenIdByFcmToken(fcmToken)

        deviceTokenRepository.upsertByFcmToken(userId, fcmToken, platform, authTokenId)

        if (previousAuthTokenId != null && previousAuthTokenId != authTokenId) {
            authTokenRepository.deleteById(previousAuthTokenId)
        }
    }
}
