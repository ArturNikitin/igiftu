package com.svetka.igiftu

import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.security.jwt.JwtConfig
import com.svetka.igiftu.security.jwt.JwtSecretKey
import com.svetka.igiftu.security.service.MyUserDetails
import com.svetka.igiftu.security.service.facebook.FacebookSignInAdapter
import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.ConnectionSignUp
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository
import org.springframework.social.connect.support.ConnectionFactoryRegistry
import org.springframework.social.connect.web.ProviderSignInController
import org.springframework.social.connect.web.SignInAdapter
import org.springframework.social.facebook.connect.FacebookConnectionFactory
import org.springframework.web.context.request.NativeWebRequest

@TestConfiguration
class TestConfiguration {
	
	@Bean
	fun userDetailsService() = UserDetailsService {
		when(it) {
			"user@gmail.com" -> MyUserDetails("user@gmail.com", "\$2a\$08\$NsQrzwudraY79SzZB0jpVe/1YZpan5EZPJ4WXGmv.bPjtDVsXrZ.q", UserRoles.ROLE_USER.toString())
			"admin@gmail.com" -> MyUserDetails("admin@gmail.com", "\$2a\$08\$NsQrzwudraY79SzZB0jpVe/1YZpan5EZPJ4WXGmv.bPjtDVsXrZ.q", "ROLE_ADMIN")
			else -> throw UsernameNotFoundException("User with email $it not found")
		}
	}
	
	@Bean
	fun jwtConfig() = JwtConfig()
	
	@Bean
	fun jwtSecretKey() = JwtSecretKey(jwtConfig())
	
	@Bean
	fun secretKey(): SecretKey = Keys.hmacShaKeyFor(jwtConfig().key.toByteArray())

	@Bean
	fun facebookConnectionSignup() = ConnectionSignUp { "usernameFacebook" }
}