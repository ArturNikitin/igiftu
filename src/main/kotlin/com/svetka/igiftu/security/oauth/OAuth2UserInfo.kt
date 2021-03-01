package com.svetka.igiftu.security.oauth

open abstract class OAuth2UserInfo(
    open var attributes: Map<String, Any>
) {
    abstract fun getId(): String

    abstract fun getName(): String

    abstract fun getEmail(): String
}