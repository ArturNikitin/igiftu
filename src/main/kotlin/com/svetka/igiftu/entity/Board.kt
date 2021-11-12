package com.svetka.igiftu.entity

import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

@Entity
@Table(name = "boards")
class Board(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "created_date")
	@CreatedDate
	var createdDate: LocalDateTime?,

	@Column(name = "last_modified_date")
	@LastModifiedDate
	var lastModifiedDate: LocalDateTime?,

	@Column(name = "name")
	var name: String,

	@ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	var user: User? = null,

	@OneToOne(cascade = [CascadeType.DETACH], fetch = FetchType.EAGER)
	@JoinColumn(name = "image_id")
	var image: Image? = null
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Board) return false

		if (id != other.id) return false
		if (name != other.name) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id?.hashCode() ?: 0
		result = 31 * result + name.hashCode()
		return result
	}

	override fun toString(): String {
		return "Board(id=$id, name='$name')"
	}
}