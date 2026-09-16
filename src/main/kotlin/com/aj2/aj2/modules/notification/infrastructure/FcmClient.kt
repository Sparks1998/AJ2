package com.aj2.aj2.modules.notification.infrastructure

interface FcmClient {
    fun push(tokens: List<String>, title: String, body: String?)
}
