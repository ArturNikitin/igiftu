package com.svetka.igiftu.config

import com.svetka.igiftu.entity.User
import com.svetka.igiftu.service.UserDto
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class MapperConfig(
	private val encoder: PasswordEncoder
) : OrikaMapperFactoryConfigurer {
	override fun configure(factory: MapperFactory) {
		factory.classMap(User::class.java, UserDto::class.java)
			.byDefault()
			.field("email", "email")
			.field("password", "password")
			.register()
	}
}