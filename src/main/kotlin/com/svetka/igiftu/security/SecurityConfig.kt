package com.svetka.igiftu.security

import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.security.jwt.JwtConfig
import com.svetka.igiftu.security.jwt.JwtTokenVerifier
import com.svetka.igiftu.security.jwt.JwtUsernameAndPasswordAuthenticationFilter
import com.svetka.igiftu.security.jwt.SecurityExceptionHandlerFilter
import com.svetka.igiftu.security.oauth.CustomSimpleUrlAuthenticationSuccessHandler
import com.svetka.igiftu.security.oauth.OAuthHandleResponseFilter
import java.security.SecureRandom
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.servlet.HandlerExceptionResolver


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
	private val userDetailsService: UserDetailsService,
	private val secretKey: SecretKey,
	private val jwtConfig: JwtConfig,
	@Qualifier("handlerExceptionResolver")
	private val resolver: HandlerExceptionResolver,
	@Qualifier("customAccessDeniedHandler")
	private val accessDeniedHandler: AccessDeniedHandler,
	@Qualifier("customAuthenticationEntryPoint")
	private val entryPoint: AuthenticationEntryPoint,
	private val onSuccess: CustomSimpleUrlAuthenticationSuccessHandler
) : WebSecurityConfigurerAdapter() {

	@Value("\${spring.social.facebook.appId}")
	private lateinit var appId: String

	@Value("\${spring.social.facebook.appSecret}")
	private lateinit var appSecret: String

	@Lazy
	@Autowired
	private lateinit var userComponent: UserComponent

	override fun configure(auth: AuthenticationManagerBuilder?) {
		auth?.userDetailsService(userDetailsService)
	}

	override fun configure(http: HttpSecurity) {
		http
			.cors().configurationSource {
				CorsConfiguration().apply {
					allowedOrigins = listOf("http://localhost:3000")
					allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
					allowedHeaders = listOf("*")
				}
			}
			.and()
			.csrf().disable()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler)
			.authenticationEntryPoint(entryPoint)
			.and()
			.addFilterBefore(
				SecurityExceptionHandlerFilter(resolver),
				JwtUsernameAndPasswordAuthenticationFilter::class.java
			)
			.addFilterAfter(
				jwtUsernameAndPasswordAuthenticationFilter(),
				OAuth2LoginAuthenticationFilter::class.java
			)
			.addFilterAfter(
				JwtTokenVerifier(jwtConfig, secretKey),
				JwtUsernameAndPasswordAuthenticationFilter::class.java
			)
			.addFilterBefore(testFilter(), OAuth2LoginAuthenticationFilter::class.java)
			.authorizeRequests()
//				SWAGGER SHIT
			.antMatchers("/swagger-ui/**").permitAll()
			.antMatchers("/swagger-resources/**").permitAll()
			.antMatchers("/v3/**").permitAll()

			.antMatchers("/user/login").authenticated()
			.antMatchers("/user/**").permitAll()

			.mvcMatchers("/auth/**").permitAll()
			.anyRequest().authenticated()

			.and()
			.oauth2Login()
			.authorizationEndpoint()
			.baseUri("/auth")
			.and()
			.successHandler(onSuccess)
	}

	private fun jwtUsernameAndPasswordAuthenticationFilter() =
		JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), secretKey, jwtConfig)
			.apply {
				this.setFilterProcessesUrl("/user/login")
				this.userService = userComponent
			}

	private fun testFilter() = OAuthHandleResponseFilter(secretKey, jwtConfig)

	@Bean
	fun passwordEncoderBean(): PasswordEncoder {
		return BCryptPasswordEncoder(8, SecureRandom())
	}
}