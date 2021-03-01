package com.svetka.igiftu.exception

import org.springframework.security.core.AuthenticationException

class OAuth2AuthenticationProcessingException : AuthenticationException {
    constructor(msg: String) : super(msg)

    constructor(msg: String, cause: Throwable) : super(msg, cause)
}