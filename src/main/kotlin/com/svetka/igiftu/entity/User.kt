package com.svetka.igiftu.entity

import com.svetka.igiftu.entity.enums.UserRoles
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Users")
data class User(
        @Id @GeneratedValue var id: Long,
        @Column(name = "created_date") var createdDate: LocalDateTime,
        var password: String,
        var email: String,
        @Enumerated var role: UserRoles,
        var isEnabled: Boolean,
        var isAccountNonLocked: Boolean,
        var login: String
)