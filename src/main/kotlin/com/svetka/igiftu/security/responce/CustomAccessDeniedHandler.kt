package com.svetka.igiftu.security.responce

import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

/**
 *
 * Handles an error when an authorized user tries to access content,
 * but doesn't have the suitable role
 *
 * */
@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AccessDeniedException
    ) {
        response.status = (HttpServletResponse.SC_FORBIDDEN)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.toString()
        response.writer.write(
            ObjectMapper()
                .writeValueAsString(Response(exception.message, "Not enough permissions to access this data"))
        )
    }
}

/**
 *
 * Handles an error when an unauthorized user tries to access content
 * which is accessible only for logged-in users
 *
 * */
@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        response.status = (HttpServletResponse.SC_UNAUTHORIZED)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.toString()
        response.writer.write(
            ObjectMapper()
                .writeValueAsString(Response(exception.message, "You must authenticate"))
        )
    }
}

data class Response(
    val error: String?,
    val message: String
)