package com.svetka.igiftu.security.service

import com.svetka.igiftu.dto.converters.UserOAuthConverter
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.AuthProvider
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.exception.OAuth2AuthenticationProcessingException
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.security.oauth.CustomAuth2User
import com.svetka.igiftu.security.oauth.FacebookOAuth2UserInfo
import com.svetka.igiftu.security.oauth.OAuth2UserInfo
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.naming.AuthenticationException

@Service
class OAuth2UserServiceImpl(
    val userRepo: UserRepository
) : DefaultOAuth2UserService(){
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val user = super.loadUser(userRequest)

        return try {
            processOAuth2User(userRequest, user)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(request: OAuth2UserRequest, user: OAuth2User) : OAuth2User {
        val userInfo = FacebookOAuth2UserInfo(user.attributes)

        if (userInfo.getEmail().isEmpty()) throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")

        val userFromDB = userInfo.getEmail().let { userRepo.getUserByEmail(it) }

        userFromDB ?: run {
            registerNewUser(request, userInfo)
        }

        return user
    }

    private fun registerNewUser(request: OAuth2UserRequest, userInfo: OAuth2UserInfo) {
        val user = UserOAuthConverter.convertUser(
            userInfo.getEmail(),
            AuthProvider.FACEBOOK,
            isEnabled =  true,
            isNonLocked =  true,
            userRole = UserRoles.ROLE_USER
        )

        user.createdDate = LocalDateTime.now()
        user.login = user.email
            .replaceAfterLast("@", "")
        userRepo.save(user)
    }
}