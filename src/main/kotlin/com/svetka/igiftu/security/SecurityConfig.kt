package com.svetka.igiftu.security

import com.svetka.igiftu.security.jwt.JwtConfig
import com.svetka.igiftu.security.jwt.JwtTokenVerifier
import com.svetka.igiftu.security.jwt.JwtUsernameAndPasswordAuthenticationFilter
import com.svetka.igiftu.security.service.UserDetailsServiceImpl
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.crypto.SecretKey

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
class SecurityConfig(
	private val userDetailsService: UserDetailsServiceImpl,
	private val secretKey: SecretKey,
	private val jwtConfig: JwtConfig
) : WebSecurityConfigurerAdapter() {

	override fun configure(auth: AuthenticationManagerBuilder?) {
		auth?.userDetailsService(userDetailsService)
	}

	override fun configure(http: HttpSecurity) {
		http.csrf().disable().addFilter(
			JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), secretKey, jwtConfig)
		)
			.addFilterAfter(
				JwtTokenVerifier(jwtConfig, secretKey),
				JwtUsernameAndPasswordAuthenticationFilter::class.java
			)
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().authorizeRequests()
			.antMatchers("/swagger-ui/**").permitAll()
			.antMatchers("/swagger-resources/**").permitAll()
			.antMatchers("/v2/api-docs**").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/registration").permitAll()
			.antMatchers("/user/**").permitAll()
			.antMatchers("/api/**").hasRole("ADMIN")
	}

	@Bean
	fun passwordEncoderBean(): PasswordEncoder {
		return BCryptPasswordEncoder(8) // оптимальная сила по скорости вычисления и уровню шифрования
	}
}