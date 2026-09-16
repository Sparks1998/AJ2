package com.aj2.aj2.infrastructure.interceptors

import com.aj2.aj2.shared.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import tools.jackson.databind.ObjectMapper

@ControllerAdvice(annotations = [RestController::class])
class ResponseInterceptor(private val objectMapper: ObjectMapper) : ResponseBodyAdvice<Any> {
    private val log = LoggerFactory.getLogger(ResponseInterceptor::class.java)

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean = true // apply to all responses

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any {
        val statusCode = (response as ServletServerHttpResponse).servletResponse.status
        val apiResponse = when {
            // If body is already ApiResponse, use it as-is
            body is ApiResponse -> body

            // For error responses with MutableMap body
            statusCode != 200 && statusCode != 201 && body is MutableMap<*, *> -> {
                ApiResponse(
                    success = body["success"] as? Boolean ?: false,
                    data = body["data"],
                    message = body["message"] as? String,
                )
            }

            // For success responses with non-ApiResponse body
            (statusCode == 200 || statusCode == 201) -> {
                ApiResponse(
                    success = true,
                    data = body,
                    message = "Request Succeeded",
                )
            }

            // Fallback for other error cases
            else -> {
                ApiResponse(
                    success = false,
                    data = null,
                    message = "An error occurred",
                )
            }
        }

        // Calculate and set Content-Length
        try {
            val jsonBytes = objectMapper.writeValueAsBytes(apiResponse)
            response.headers.add("X_CONTENT_LENGTH", "${jsonBytes.size.toLong()}")
        } catch (e: Exception) {
            log.error("Failed to calculate content length", e)
        }

        return apiResponse
    }
}
