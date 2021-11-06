package com.svetka.igiftu.service.entity.impl

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
import org.springframework.transaction.annotation.Transactional

@Service
class ImageServiceImpl(
	private val s3StorageService: StorageService,
	private val imageRepository: ImageRepository
) : ImageService {
	companion object {
		val simpleCash = mapOf(
			"default-wish" to Files.readAllBytes(
				Paths.get("src/main/resources/static/pictures/wish.jpeg")
			),
			"default-user-pic" to Files.readAllBytes(
				Paths.get("src/main/resources/static/pictures/user_pic.png")
			)
		)
	}

	@Transactional
	override fun saveImage(imageName: String, content: ByteArray): ImageDto {
		CompletableFuture<String>().completeAsync {
			s3StorageService.putFile(content, imageName)
		}

		val image = imageRepository.save(Image(name = imageName))

		return ImageDto(id = image.id,
			name = image.name,
			content = content)
	}

	@Transactional
	override fun getImage(imageName: String): ImageDto {
		val content = simpleCash[imageName] ?: s3StorageService.getFile(imageName)

		val image = imageRepository.findByName(imageName)
			.orElseThrow { EntityNotFoundException("Image with name $imageName not found") }

		return ImageDto(id = image.id,
			name = image.name,
			content = content)
	}

	override fun getContent(imageName: String): ByteArray {
		return simpleCash[imageName] ?: s3StorageService.getFile(imageName)
	}
}