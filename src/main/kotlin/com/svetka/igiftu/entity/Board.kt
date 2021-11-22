package com.svetka.igiftu.entity

import java.time.LocalDateTime
import javax.persistence.CascadeType.ALL
import javax.persistence.CascadeType.DETACH
import javax.persistence.CascadeType.PERSIST
import javax.persistence.Column
import javax.persistence.Entity
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

	@ManyToOne(cascade = [PERSIST], fetch = LAZY)
	@JoinColumn(name = "user_id")
	var user: User? = null,

	@OneToOne(cascade = [ALL], fetch = EAGER)
	@JoinColumn(name = "image_id")
	var image: Image? = null,

	@ManyToMany(cascade = [PERSIST], fetch = LAZY)
	@JoinTable(
		name = "wishes_boards",
		joinColumns = [JoinColumn(name = "board_id")],
		inverseJoinColumns = [JoinColumn(name = "wish_id")]
	)
	val wishes: MutableSet<Wish> = mutableSetOf()
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