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
open class User(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "created_date")
	@CreatedDate
	var createdDate: LocalDateTime? = null,

	@Column
	var password: String,

	@Column
	var email: String,

	@Column
	var login: String? = null,

	@Column
	@Enumerated(value = EnumType.STRING)
	var role: UserRoles? = null,

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
		if (password != other.password) return false
		if (email != other.email) return false
		if (role != other.role) return false
		if (isEnabled != other.isEnabled) return false
		if (isAccountNonLocked != other.isAccountNonLocked) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id?.hashCode() ?: 0
		result = 31 * result + password.hashCode()
		result = 31 * result + email.hashCode()
		result = 31 * result + (role?.hashCode() ?: 0)
		result = 31 * result + isEnabled.hashCode()
		result = 31 * result + isAccountNonLocked.hashCode()
		return result
	}

	override fun toString(): String {
		return "User(id=$id, email='$email', login=$login, role=$role, isEnabled=$isEnabled, isAccountNonLocked=$isAccountNonLocked)"
	}

}