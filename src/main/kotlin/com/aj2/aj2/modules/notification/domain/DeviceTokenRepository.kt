package com.aj2.aj2.modules.notification.domain

import java.util.UUID

interface DeviceTokenRepository {
    fun findByUserId(userId: UUID): List<DeviceToken>
    fun save(deviceToken: DeviceToken): DeviceToken
}
