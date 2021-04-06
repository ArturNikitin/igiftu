package com.svetka.igiftu.entity

import com.svetka.igiftu.entity.enums.UserRoles
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Users")
class User(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long?,

	@Column(name = "created_date")
	@CreatedDate
	var createdDate: LocalDateTime?,

	@Column
	var password: String,

	@Column
	var email: String,

	@Column
	var login: String?,

	@Column
	@Enumerated(value = EnumType.STRING)
	var role: UserRoles?,

	@Column(name = "isenabled")
	var isEnabled: Boolean,

	@Column(name = "isaccountnonlocked")
	var isAccountNonLocked: Boolean
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as User

		if (id != other.id) return false
		if (email != other.email) return false
		if (login != other.login) return false
		if (role != other.role) return false
		if (isEnabled != other.isEnabled) return false
		if (isAccountNonLocked != other.isAccountNonLocked) return false

		return true
	}

	override fun hashCode(): Int {
		return id?.hashCode() ?: 0
	}

	override fun toString(): String {
		return "User(id=$id, createdDate=$createdDate, email='$email', login=$login," +
				" role=$role, isEnabled=$isEnabled, isAccountNonLocked=$isAccountNonLocked)"
	}


}