package com.svetka.igiftu.security.oauth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomAuth2User(
    val oauthUser: OAuth2User
) : OAuth2User {

    override fun getName(): String {
        return oauthUser.name
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return oauthUser.attributes
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return oauthUser.authorities
    }
}