package com.svetka.igiftu.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ClassUtils
import springfox.documentation.RequestHandler
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket


@Configuration
class SwaggerConfig {

	@Bean
	fun swagger(): Docket {
		return Docket(DocumentationType.OAS_30)
			.securityContexts(listOf(securityContext()))
			.securitySchemes(listOf(apiKey()))
			.select()
			.apis { input: RequestHandler -> listOf("com.svetka.igiftu.controller", "org.springframework.social.connect.web")
				.any { ClassUtils.getPackageName(input.declaringClass())
					.startsWith(it) } }
			.paths(PathSelectors.any())
			.build()
	}

	private fun apiKey() = ApiKey("Authorization", "Authorization", "header")


	private fun securityContext() =
		SecurityContext.builder().securityReferences(defaultAuth()).build()


	private fun defaultAuth(): List<SecurityReference> {
		val authorizationScope = AuthorizationScope("global", "accessEverything")
		val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls(1)
		authorizationScopes[0] = authorizationScope
		return listOf(SecurityReference("Authorization", authorizationScopes))
	}
}