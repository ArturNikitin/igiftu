package com.svetka.igiftu.security.oauth

class FacebookOAuth2UserInfo(
    override var attributes: Map<String, Any>
): OAuth2UserInfo(attributes) {

    override fun getId(): String {
       return attributes["id"].toString()
    }

    override fun getName(): String {
        return attributes["name"].toString()
    }

    override fun getEmail(): String {
        return attributes["email"].toString()
    }
}