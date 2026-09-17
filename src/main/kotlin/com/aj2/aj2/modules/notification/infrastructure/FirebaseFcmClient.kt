package com.aj2.aj2.modules.notification.infrastructure

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.ApsAlert
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

/**
 * FCM push client backed by the Firebase Admin SDK. Mirrors JwtSupport's
 * keystore handling: if no credentials file is present/readable/valid yet,
 * it logs a warning and falls back to logging pushes instead of sending
 * them, so the app still boots before Firebase credentials are provisioned.
 */
@Component
class FirebaseFcmClient(
    @Value($$"${firebase.credentials-location:file:./firebase-service-account.json}") credentialsLocation: Resource,
) : FcmClient {
    private val log = LoggerFactory.getLogger(FirebaseFcmClient::class.java)
    private val firebaseApp: FirebaseApp? = initFirebaseApp(credentialsLocation)

    private fun initFirebaseApp(credentialsLocation: Resource): FirebaseApp? {
        if (!credentialsLocation.exists() || !credentialsLocation.isReadable) {
            log.warn(
                "FCM push: no Firebase credentials found at '{}' - push notifications are disabled (stub mode)",
                credentialsLocation,
            )
            return null
        }

        return try {
            val options = FirebaseOptions.builder()
                .setCredentials(credentialsLocation.inputStream.use { GoogleCredentials.fromStream(it) })
                .build()

            val app = FirebaseApp.getApps().find { it.name == FirebaseApp.DEFAULT_APP_NAME }
                ?: FirebaseApp.initializeApp(options)

            log.info("FCM push: Firebase Admin SDK initialized from '{}'", credentialsLocation)
            app
        } catch (e: Exception) {
            log.warn(
                "FCM push: failed to initialize Firebase Admin SDK from '{}': {}. Push notifications are disabled (stub mode).",
                credentialsLocation,
                e.message,
            )
            null
        }
    }

    override fun push(
        tokens: List<String>,
        title: String,
        body: String?,
        titleLocKey: String?,
        bodyLocKey: String?,
        bodyLocArgs: List<String>?,
    ) {
        if (tokens.isEmpty()) return

        val app = firebaseApp
        if (app == null) {
            log.info(
                "FCM push (stub) to {} device(s): title='{}' body='{}' titleLocKey='{}' bodyLocKey='{}' bodyLocArgs={}",
                tokens.size,
                title,
                body,
                titleLocKey,
                bodyLocKey,
                bodyLocArgs,
            )
            return
        }

        // Platform-specific blocks only (no top-level Notification): that's the
        // FCM payload shape that actually supports title_loc_key/body_loc_key so
        // each device renders the notification in its own language.
        val androidNotification = AndroidNotification.builder()
            .setTitle(title)
            .apply { body?.let { setBody(it) } }
            .apply { titleLocKey?.let { setTitleLocalizationKey(it) } }
            .apply { bodyLocKey?.let { setBodyLocalizationKey(it) } }
            .apply { bodyLocArgs?.let { addAllBodyLocalizationArgs(it) } }
            .build()

        val apsAlert = ApsAlert.builder()
            .setTitle(title)
            .apply { body?.let { setBody(it) } }
            .apply { titleLocKey?.let { setTitleLocalizationKey(it) } }
            .apply { bodyLocKey?.let { setLocalizationKey(it) } }
            .apply { bodyLocArgs?.let { addAllLocalizationArgs(it) } }
            .build()

        val message = MulticastMessage.builder()
            .addAllFids(tokens)
            .setAndroidConfig(AndroidConfig.builder().setNotification(androidNotification).build())
            .setApnsConfig(ApnsConfig.builder().setAps(Aps.builder().setAlert(apsAlert).build()).build())
            .build()

        try {
            val response = FirebaseMessaging.getInstance(app).sendEachForMulticast(message)
            if (response.failureCount > 0) {
                log.warn("FCM push: {} of {} device(s) failed", response.failureCount, tokens.size)
            }
        } catch (e: Exception) {
            log.warn("FCM push failed: {}", e.message)
        }
    }
}
