package com.svetka.igiftu.entity

import com.svetka.igiftu.entity.enums.Access
import java.time.LocalDateTime
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
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

@Entity
@Table(name = "Wishes")
class Wish(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,
	
	@Column(name = "created_date")
	@CreatedDate
	val createdDate: LocalDateTime,
	
	@Column(name = "last_modified_date")
	@LastModifiedDate
	var lastModifiedDate: LocalDateTime,
	
	@Column
	var name: String,
	
	@Column(name = "is_booked")
	var isBooked: Boolean = false,
	
	@Column(name = "is_completed")
	var isCompleted: Boolean = false,
	
	@Column(name = "is_analog_possible")
	var isAnalogPossible: Boolean = true,
	
	@Column
	@Enumerated(value = EnumType.STRING)
	var access: Access = Access.PUBLIC,
	
	@Column
	var price: Double? = null,
	
	@Column
	var location: String? = null,
	
	@Column
	var details: String? = null,
	
	@Column
	var link: String? = null,
	
	@ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val user: User
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