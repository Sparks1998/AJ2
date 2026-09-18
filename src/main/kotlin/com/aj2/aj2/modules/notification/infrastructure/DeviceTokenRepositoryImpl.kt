package com.aj2.aj2.modules.notification.infrastructure

import com.aj2.aj2.modules.notification.domain.DevicePlatform
import com.aj2.aj2.modules.notification.domain.DeviceToken
import com.aj2.aj2.modules.notification.domain.DeviceTokenRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class DeviceTokenRepositoryImpl(
    private val jpaRepository: DeviceTokenJpaRepository,
) : DeviceTokenRepository {
    override fun findByUserId(userId: UUID): List<DeviceToken> = jpaRepository.findByUserId(userId)

    override fun findAuthTokenIdByFcmToken(fcmToken: String): UUID? =
        jpaRepository.findByFcmToken(fcmToken)?.authToken?.id

    override fun upsertByFcmToken(userId: UUID, fcmToken: String, platform: DevicePlatform, authTokenId: UUID) =
        jpaRepository.upsertByFcmToken(userId, fcmToken, authTokenId, platform.name)

    override fun save(deviceToken: DeviceToken): DeviceToken = jpaRepository.save(deviceToken)
}
