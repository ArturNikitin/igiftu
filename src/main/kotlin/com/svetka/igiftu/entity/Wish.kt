package com.svetka.igiftu.entity

import com.svetka.igiftu.entity.enums.Access
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "Wishes")
class Wish(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,
	
	@Column
	var name: String,
	
	@Column
	var price: Double? = null,
	
	@Column
	@Enumerated(value = EnumType.STRING)
	var access: Access = Access.PUBLIC,
	
	@ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val user: User? = null
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as Wish
		
		if (id != other.id) return false
		if (name != other.name) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = id?.hashCode() ?: 0
		result = 31 * result + name.hashCode()
		return result
	}
}