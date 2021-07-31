package com.svetka.igiftu.service.impl

import com.svetka.igiftu.service.SecurityManager
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class SecurityManagerImpl : SecurityManager {
	//	todo
	override fun isModificationAllowed(userId: Long, contentId: Long, principal: Principal): Boolean {
		return true
	}

	//  todo
	override fun isCreationAllowed(userId: Long, principal: Principal): Boolean {
		return true
	}
}