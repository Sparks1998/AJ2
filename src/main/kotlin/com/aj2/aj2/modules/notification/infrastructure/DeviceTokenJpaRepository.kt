package com.aj2.aj2.modules.notification.infrastructure

import com.aj2.aj2.modules.notification.domain.DeviceToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface DeviceTokenJpaRepository : JpaRepository<DeviceToken, UUID> {
    fun findByUserId(userId: UUID): List<DeviceToken>
    fun findByFcmToken(fcmToken: String): DeviceToken?

    // Single atomic statement on purpose: a delete-then-insert pair breaks under
    // Hibernate's flush ordering (inserts always flush before deletes within one
    // flush, regardless of call order), which re-violates the fcm_token unique
    // constraint. ON CONFLICT also makes this safe under concurrent requests for
    // the same token, which delete-then-insert never was either.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        value = """
            INSERT INTO device_tokens (id, user_id, fcm_token, auth_token_id, platform, last_used_at)
            VALUES (gen_random_uuid(), :userId, :fcmToken, :authTokenId, CAST(:platform AS device_platform), now())
            ON CONFLICT (fcm_token) DO UPDATE
            SET user_id = EXCLUDED.user_id,
                auth_token_id = EXCLUDED.auth_token_id,
                platform = EXCLUDED.platform,
                last_used_at = EXCLUDED.last_used_at
        """,
        nativeQuery = true,
    )
    fun upsertByFcmToken(
        @Param("userId") userId: UUID,
        @Param("fcmToken") fcmToken: String,
        @Param("authTokenId") authTokenId: UUID,
        @Param("platform") platform: String,
    )
}
