package com.svetka.igiftu.aop

import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.dto.UserInfo
import com.svetka.igiftu.exceptions.SecurityModificationException
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Aspect
@Component
class SecurityAspect(
	private val userComponent: UserComponent
) {
	private val logger = KotlinLogging.logger {  }

	@Before("@annotation(com.svetka.igiftu.aop.ModificationPermissionRequired)")
	fun isModificationAllowed(joinPoint: JoinPoint) {
			val signature = joinPoint.signature as MethodSignature
		signature.parameterNames
		val userInfo = joinPoint.args.filterIsInstance<UserInfo>().first()
		logger.debug { "Checking if user with id [${userInfo.id}] is same as [${userInfo.username}]" }
		val differentUser = !userComponent.isSameUser(userInfo.id, userInfo.username)
		if (differentUser)
			 throw SecurityModificationException("Illegal modification attempt by user [${userInfo.username}]")
	}
}