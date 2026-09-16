package com.aj2.aj2.modules.notification.infrastructure

import com.aj2.aj2.modules.notification.domain.DeviceToken
import com.aj2.aj2.modules.notification.domain.DeviceTokenRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class DeviceTokenRepositoryImpl(
    private val jpaRepository: DeviceTokenJpaRepository,
) : DeviceTokenRepository {
    override fun findByUserId(userId: UUID): List<DeviceToken> = jpaRepository.findByUserId(userId)

    override fun save(deviceToken: DeviceToken): DeviceToken = jpaRepository.save(deviceToken)
}
