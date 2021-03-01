package com.svetka.igiftu.dto.converters

import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.AuthProvider
import com.svetka.igiftu.entity.enums.UserRoles

object UserOAuthConverter {
    fun convertUser(
        email: String,
        provider: AuthProvider,
        isNonLocked: Boolean,
        isEnabled: Boolean,
        userRole: UserRoles
    ): User {
        return User(
            email = email,
            login = null,
            id = null,
            createdDate = null,
            password = null,
            provider = provider,
            isAccountNonLocked = isNonLocked,
            isEnabled = isEnabled,
            role = userRole
        )
    }
}