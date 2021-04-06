package com.svetka.igiftu.config

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class MapperConfig(
    private val encoder: PasswordEncoder
) : OrikaMapperFactoryConfigurer
{
    override fun configure(factory: MapperFactory) {
        factory.classMap(User::class.java, UserDto::class.java)
            .field("email", "email")
            .field("login", "login")
            .register()
    }
}