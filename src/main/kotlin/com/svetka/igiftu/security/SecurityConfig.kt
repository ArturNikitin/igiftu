package com.svetka.igiftu.security

import com.svetka.igiftu.security.jwt.*
import com.svetka.igiftu.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository
import com.svetka.igiftu.security.oauth.OAuth2AuthenticationSuccessHandler
import com.svetka.igiftu.security.service.OAuth2UserServiceImpl
import com.svetka.igiftu.security.service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.crypto.SecretKey


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val userDetailsService: UserDetailsServiceImpl,
    private val unauthorizedHandler : JwtAuthenticationEntryPoint,
    private val tokenProvider: TokenProvider,
    private val oAuth2AuthorizationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2UserServiceImpl: OAuth2UserServiceImpl,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
            .userDetailsService<UserDetailsService>(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder(8)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/api/auth/**, /oauth2/**")
                    .permitAll()
                .antMatchers("/user/1")
                    .permitAll()
                .antMatchers("/oauth2/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
            .and()
                .oauth2Login()
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(oAuth2UserServiceImpl)
            .and()
            .successHandler(oAuth2AuthorizationSuccessHandler)


        // Add our custom JWT security filter
        http.addFilter(
                    getJwtFilter()
                )
                .addFilterAfter(
                    JwtTokenVerifier(tokenProvider),
                    JwtUsernameAndPasswordAuthenticationFilter::class.java
                )
    }

    fun getJwtFilter(): JwtUsernameAndPasswordAuthenticationFilter {
            return JwtUsernameAndPasswordAuthenticationFilter(
                authenticationManager(),
                tokenProvider
            ).apply { setFilterProcessesUrl("/basic/login") }
        }



//    @Configuration
//    @Order(2)
//    class JwtSecurityConfig(
//        private val userDetailsService: UserDetailsServiceImpl,
//        private val secretKey: SecretKey,
//        private val jwtConfig: JwtConfig
//    ) : WebSecurityConfigurerAdapter() {
//        override fun configure(auth: AuthenticationManagerBuilder?) {
//            auth?.userDetailsService(userDetailsService)
//        }
//
//        override fun configure(http: HttpSecurity) {
//            http.csrf()
//                .disable()
//                .addFilter(
//                    getJwtFilter()
//                )
//                .addFilterAfter(
//                    JwtTokenVerifier(jwtConfig, secretKey),
//                    JwtUsernameAndPasswordAuthenticationFilter::class.java
//                )
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().authorizeRequests()
//                .antMatchers("/swagger-ui/**").permitAll()
//                .antMatchers("/swagger-resources/**").permitAll()
//                .antMatchers("/v2/api-docs**").permitAll()
//                .antMatchers("/basic/login").permitAll()
//                .antMatchers("/registration").permitAll()
//                .antMatchers("/user/**").permitAll()
//                .antMatchers("/api/**").hasRole("ADMIN")
//        }
//
//        fun getJwtFilter(): JwtUsernameAndPasswordAuthenticationFilter {
//            return JwtUsernameAndPasswordAuthenticationFilter(
//                authenticationManager(),
//                secretKey,
//                jwtConfig
//            ).apply { setFilterProcessesUrl("/basic/login") }
//        }
//    }
//
//    /**
//     * Дополнительная концигурация для аутентификации через facebook
//     * @url для переадресации на фб - {HOST}/oauth2/authorization/facebook
//     * */
//    @Configuration
//    @Order(1)
//    class AuthSecurityConfig : WebSecurityConfigurerAdapter() {
//
//        override fun configure(http: HttpSecurity) {
//            http.csrf()
//                .disable()
////			.addFilter(
////			JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), secretKey, jwtConfig)
////		)
////			.addFilterAfter(
////				JwtTokenVerifier(jwtConfig, secretKey),
////				JwtUsernameAndPasswordAuthenticationFilter::class.java
////			)
//				.antMatcher("/oauth2/**")
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login()
//        }
//
////        @Bean
////        fun authorizationRequestRepository(): AuthorizationRequestRepository<OAuth2AuthorizationRequest?>? {
////            return HttpSessionOAuth2AuthorizationRequestRepository()
////        }
////    }
//
//
//        @Bean
//        fun passwordEncoderBean(): PasswordEncoder {
//            return BCryptPasswordEncoder(8) // оптимальная сила по скорости вычисления и уровню шифрования
//        }
//    }
}


