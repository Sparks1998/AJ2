package com.aj2.aj2.shared

data class ApiResponse(
    val success: Boolean = false,
    val message: String? = null,
    val data: Any? = null,
)
