package com.aj2.aj2.modules.notification.infrastructure

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Stub FCM adapter: logs the push instead of calling Firebase.
 * Swap for a real Firebase Admin SDK-backed implementation when credentials are available.
 */
@Component
class NoopFcmClient : FcmClient {
    private val log = LoggerFactory.getLogger(NoopFcmClient::class.java)

    override fun push(tokens: List<String>, title: String, body: String?) {
        if (tokens.isEmpty()) return
        log.info("FCM push (stub) to {} device(s): title='{}' body='{}'", tokens.size, title, body)
    }
}
