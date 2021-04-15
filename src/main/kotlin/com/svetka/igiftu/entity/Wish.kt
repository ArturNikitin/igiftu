package com.svetka.igiftu.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
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
	
	val name: String,
	
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