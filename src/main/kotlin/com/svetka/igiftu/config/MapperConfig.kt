package com.svetka.igiftu.config

import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class MapperConfig(
    private val encoder: PasswordEncoder
) : OrikaMapperFactoryConfigurer {
    override fun configure(factory: MapperFactory) {

    }
}