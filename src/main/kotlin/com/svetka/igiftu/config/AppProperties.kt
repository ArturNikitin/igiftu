package com.svetka.igiftu.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app")
class AppProperties(
    var auth: Auth,
    var oauth2: Oauth2
) {
    constructor() : this(Auth(), Oauth2())
    class Auth(
        var tokenSecret: String,
        var tokenExpirationMsec: Long
    ) {
        constructor() : this("", 0L)
    }

    class Oauth2(
        var authorizedRedirectUris: List<String>
    ) {
        constructor() : this(emptyList())
    }
}

