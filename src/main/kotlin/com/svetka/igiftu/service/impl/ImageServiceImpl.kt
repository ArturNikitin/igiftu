package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.entity.Image
import com.svetka.igiftu.repository.ImageRepository
import com.svetka.igiftu.service.common.StorageService
import com.svetka.igiftu.service.entity.ImageService
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import javax.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class ImageServiceImpl(
	private val s3StorageService: StorageService,
	private val imageRepository: ImageRepository
) : ImageService {
	companion object {
		val simpleCash = mapOf(
			"default-wish" to Files.readAllBytes(
				Paths.get("src/main/resources/static/pictures/wish.jpeg")
			)
		)
	}

//	TODO refactor
	override fun saveImage(imageDto: ImageDto): ImageDto {
		CompletableFuture<String>().completeAsync {
			s3StorageService.putFile(imageDto.content!!.toByteArray(), imageDto.name!!)
		}
		val image = imageRepository.save(Image(name = imageDto.name!!))
		return ImageDto(id = image.id, name = image.name, content = imageDto.content)
	}

	override fun getImage(imageName: String): ImageDto {
		val content = simpleCash[imageName] ?: s3StorageService.getFile(imageName)
		val image = imageRepository.findByName(imageName)
			.orElseThrow { EntityNotFoundException("Image with name $imageName not found") }
		return ImageDto(id = image.id, name = image.name, content = content.contentToString())
	}
}