package com.svetka.igiftu.security.service

import com.svetka.igiftu.security.oauth.CustomAuth2User
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserServiceImpl : DefaultOAuth2UserService(){
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val user = super.loadUser(userRequest)
        return CustomAuth2User(user)

    }
}