package com.svetka.igiftu.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.component.user.UserService
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component

const val PREFIX = "Bearer"
const val EXPIRATION_TIME = 864000000 // 1 day

class JwtUsernameAndPasswordAuthenticationFilter(
    private val manager: AuthenticationManager,
    private val secretKey: SecretKey,
    private val jwtConfig: JwtConfig
) : UsernamePasswordAuthenticationFilter() {

    var userService: UserComponent? = null

    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {
        return try {
            val credentials = ObjectMapper()
                .readValue(request!!.inputStream, UserCredentials::class.java)
            val authentication = UsernamePasswordAuthenticationToken(
                credentials.email,
                credentials.password
            )
            manager.authenticate(authentication)
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain, authResult: Authentication
    ) {
        val token = Jwts.builder()
            .setSubject(authResult.name)
            .claim("authorities", authResult.authorities)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(secretKey)
            .compact()

        response.status = (HttpServletResponse.SC_OK)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.toString()
        response.addHeader(
            jwtConfig.getAuthorizationHeader(),
            "$PREFIX $token"
        )
        response.addHeader("Access-Control-Expose-Headers", "Authorization")

        val user = userService!!.findUser(authResult.name)

        response.writer.write(ObjectMapper()
            .writeValueAsString(LoggedInUser(user.id, user.email, user.role?: "")))
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        response.status = (HttpServletResponse.SC_UNAUTHORIZED)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.toString()
        response.writer.write(ObjectMapper()
            .writeValueAsString(AuthFailedResponse(failed.message, "User doesn't exist or password is incorrect")))

    }
}

data class AuthFailedResponse(
    val error: String?,
    val message: String
)
data class SuccessResponse(
    val message: String,
    val username: String,
)

data class LoggedInUser(
    val userId: Long,
    val username: String,
    val userRole: String
)

data class UserCredentials(
    val email: String,
    val password: String,
    val role: String
) {
    constructor() : this("", "", "")
}