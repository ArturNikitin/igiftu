package com.svetka.igiftu.entity

import com.svetka.igiftu.entity.enums.Access
import java.time.LocalDateTime
import javax.persistence.CascadeType.ALL
import javax.persistence.CascadeType.PERSIST
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType.EAGER
import javax.persistence.FetchType.LAZY
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
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
	var createdDate: LocalDateTime,

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

//	@Column
//	var price: Double? = null,

	@Embedded
	var price: Price? = null,

	@Column
	var location: String? = null,

	@Column
	var details: String? = null,

	@Column
	var link: String? = null,

	@Column(name = "buying_myself")
	var buyingMyself: Boolean = false,

	@ManyToOne(cascade = [PERSIST], fetch = LAZY)
	@JoinColumn(name = "user_id")
	var user: User? = null,

	@ManyToOne(cascade = [ALL], fetch = EAGER)
	@JoinColumn(name = "image_id")
	var image: Image? = null,

	@ManyToMany(cascade = [PERSIST])
	@JoinTable(
		name = "wishes_boards",
		joinColumns = [JoinColumn(name = "wish_id")],
		inverseJoinColumns = [JoinColumn(name = "board_id")]
	)
	var boards: MutableSet<Board> = mutableSetOf()
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