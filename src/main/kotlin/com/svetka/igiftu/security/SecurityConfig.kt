package com.svetka.igiftu.security

import com.svetka.igiftu.security.jwt.SecurityExceptionHandlerFilter
import com.svetka.igiftu.security.jwt.JwtConfig
import com.svetka.igiftu.security.jwt.JwtTokenVerifier
import com.svetka.igiftu.security.jwt.JwtUsernameAndPasswordAuthenticationFilter
import com.svetka.igiftu.security.service.facebook.FacebookSignInAdapter
import javax.crypto.SecretKey
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.ConnectionSignUp
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository
import org.springframework.social.connect.support.ConnectionFactoryRegistry
import org.springframework.social.connect.web.ProviderSignInController
import org.springframework.social.facebook.connect.FacebookConnectionFactory
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.servlet.HandlerExceptionResolver


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val secretKey: SecretKey,
    private val jwtConfig: JwtConfig,
    private val facebookSignUp: ConnectionSignUp,
    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver
) : WebSecurityConfigurerAdapter() {

    @Value("\${spring.social.facebook.appId}")
    private lateinit var appId: String

    @Value("\${spring.social.facebook.appSecret}")
    private lateinit var appSecret: String

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)
    }

    override fun configure(http: HttpSecurity) {
        http.cors().configurationSource {
            CorsConfiguration().apply {
                allowedOrigins = listOf("http://localhost:3000")
                allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
                allowedHeaders = listOf("*")
            }
        }
            .and()
            .csrf().disable()
            .addFilterBefore(
                SecurityExceptionHandlerFilter(resolver),
                JwtUsernameAndPasswordAuthenticationFilter::class.java
            )
            .addFilter(
                jwtUsernameAndPasswordAuthenticationFilter()
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
            .antMatchers("/user/login").authenticated()
            .antMatchers("/registration").permitAll()
            .antMatchers("/user/**").permitAll()
            .antMatchers("/signin/**").permitAll()
            .antMatchers("/api/**").hasRole("ADMIN")
    }

    private fun jwtUsernameAndPasswordAuthenticationFilter() =
        JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), secretKey, jwtConfig)
            .apply { this.setFilterProcessesUrl("/user/login") }

    @Bean
    fun passwordEncoderBean(): PasswordEncoder {
        return BCryptPasswordEncoder(8) // оптимальная сила по скорости вычисления и уровню шифрования
    }

    @Bean
    fun providerSignInController(): ProviderSignInController {
        val connectionFactoryLocator = connectionFactoryLocator()
        val usersConnectionRepository = getUsersConnectionRepository(connectionFactoryLocator)
        (usersConnectionRepository as InMemoryUsersConnectionRepository)
            .setConnectionSignUp(facebookSignUp)
        return ProviderSignInController(
            connectionFactoryLocator,
            usersConnectionRepository, FacebookSignInAdapter()
        )
    }

    private fun connectionFactoryLocator(): ConnectionFactoryLocator {
        val registry = ConnectionFactoryRegistry()
        registry.addConnectionFactory(FacebookConnectionFactory(appId, appSecret))
        return registry
    }

    private fun getUsersConnectionRepository(connectionFactoryLocator: ConnectionFactoryLocator): UsersConnectionRepository {
        return InMemoryUsersConnectionRepository(connectionFactoryLocator)
    }
}