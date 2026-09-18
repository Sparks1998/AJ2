package com.aj2.aj2.modules.notification.domain

import java.util.UUID

interface DeviceTokenRepository {
    fun findByUserId(userId: UUID): List<DeviceToken>
    fun findAuthTokenIdByFcmToken(fcmToken: String): UUID?
    fun upsertByFcmToken(userId: UUID, fcmToken: String, platform: DevicePlatform, authTokenId: UUID)
    fun save(deviceToken: DeviceToken): DeviceToken
}
