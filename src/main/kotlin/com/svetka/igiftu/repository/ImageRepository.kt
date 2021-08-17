package com.svetka.igiftu.repository

import com.svetka.igiftu.entity.Image
import java.util.Optional
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : CrudRepository<Image,Long> {
	fun findByName(name: String) : Optional<Image>
}