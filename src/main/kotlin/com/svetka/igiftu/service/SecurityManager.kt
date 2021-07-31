package com.svetka.igiftu.service

import org.springframework.stereotype.Service
import java.security.Principal

@Service
interface SecurityManager {
	fun isModificationAllowed(userId: Long, contentId: Long, principal: Principal) : Boolean
	fun isCreationAllowed(userId: Long, principal: Principal): Boolean
}