package com.svetka.igiftu.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "Images")
class Image(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(name = "file_type")
	val type: String? = null,

	@Column(name = "file_name", nullable = true)
	val name: String
)