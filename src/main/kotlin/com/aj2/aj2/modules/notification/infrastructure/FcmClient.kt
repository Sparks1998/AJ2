package com.aj2.aj2.modules.notification.infrastructure

interface FcmClient {
    /**
     * [titleLocKey]/[bodyLocKey] (+ [bodyLocArgs]) map to FCM's title_loc_key/body_loc_key
     * localization fields, letting the receiving device render the notification in its
     * own language from its bundled string resources. [title]/[body] remain the French
     * fallback used when the client has no matching localized string.
     */
    fun push(
        tokens: List<String>,
        title: String,
        body: String?,
        titleLocKey: String? = null,
        bodyLocKey: String? = null,
        bodyLocArgs: List<String>? = null,
    )
}
