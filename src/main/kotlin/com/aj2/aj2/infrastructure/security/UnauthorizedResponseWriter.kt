package com.aj2.aj2.infrastructure.security

import com.aj2.aj2.shared.ApiResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class UnauthorizedResponseWriter(
    private val objectMapper: ObjectMapper,
) {
    fun write(response: HttpServletResponse, message: String) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = APPLICATION_JSON_VALUE
        response.characterEncoding = Charsets.UTF_8.name()
        response.writer.use { out ->
            out.write(objectMapper.writeValueAsString(ApiResponse(message = message)))
        }
    }
}
