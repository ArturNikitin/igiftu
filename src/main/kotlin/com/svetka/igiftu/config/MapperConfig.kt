package com.svetka.igiftu.config

import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Board
import com.svetka.igiftu.entity.Image
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.Access
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import ma.glasnost.orika.MapperFactory
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.converter.BidirectionalConverter
import ma.glasnost.orika.metadata.Type
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


private val format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

@Component
class MapperConfig(
	private val encoder: PasswordEncoder
) : OrikaMapperFactoryConfigurer {
	override fun configure(factory: MapperFactory) {
		val converterFactory = factory.converterFactory

		converterFactory.apply {
			registerConverter(UserConverter())
			registerConverter(WishConverter())
			registerConverter(BoardConverter())
			registerConverter(ImageConverter())
		}
	}

	class BoardConverter : BidirectionalConverter<Board, BoardDto>() {
		override fun convertTo(
			source: Board,
			destinationTtype: Type<BoardDto>?,
			mappingContext: MappingContext?
		): BoardDto {

			return BoardDto(
				id = source.id,
				name = source.name,
				createdDate = source.createdDate?.format(format),
				lastModifiedDate = source.lastModifiedDate?.format(format),
				image = source.image?.let { ImageDto.fill(it.name) },
				wishes = source.wishes.map { mapperFacade.map(it, WishDto::class.java) }.toSet()
			)
		}

		override fun convertFrom(
			source: BoardDto,
			destinationType: Type<Board>,
			mappingContext: MappingContext
		): Board {
			return Board(
				id = source.id,
				createdDate = source.createdDate?.let { LocalDateTime.parse(source.createdDate, format) },
				lastModifiedDate = source.lastModifiedDate?.let { LocalDateTime.parse(source.lastModifiedDate, format) },
				name = source.name,
				image = source.image?.let { Image(source.id, null, name = source.name) },
				wishes = source.wishes.map { mapperFacade.map(it, Wish::class.java) }.toMutableSet()
			)
		}
	}

	class ImageConverter : BidirectionalConverter<Image, ImageDto>() {
		override fun convertTo(
			source: Image,
			destinationType: Type<ImageDto>,
			mappingContext: MappingContext
		): ImageDto {
			return ImageDto(
				id = source.id,
				name = source.name,
				content = null
			)
		}

		override fun convertFrom(
			source: ImageDto,
			destinationType: Type<Image>,
			mappingContext: MappingContext
		): Image {
			return Image(
				id = source.id,
				name = source.name ?: ""
			)
		}
	}

	class WishConverter : BidirectionalConverter<Wish, WishDto>() {
		override fun convertTo(
			source: Wish,
			destinationTtype: Type<WishDto>?,
			mappingContext: MappingContext?
		): WishDto {

			return WishDto(
				id = source.id,
				name = source.name,
				price = source.price,
				access = source.access.toString(),
				createdDate = source.createdDate.format(format),
				lastModifiedDate = source.lastModifiedDate.format(format),
				isBooked = source.isBooked,
				isAnalogPossible = source.isAnalogPossible,
				isCompleted = source.isCompleted,
				image = source.image?.let { ImageDto.fill(it.name) }
			)
		}

		override fun convertFrom(
			source: WishDto,
			destinationType: Type<Wish>,
			mappingContext: MappingContext
		): Wish {
			return Wish(
				id = source.id,
				createdDate = LocalDateTime.parse(source.createdDate, format),
				lastModifiedDate = LocalDateTime.parse(source.lastModifiedDate, format),
				name = source.name,
				price = source.price,
				access = Access.valueOf(source.access),
				image = source.image?.let { mapperFacade.map(it, Image::class.java) }
			)
		}
	}

	class UserConverter : BidirectionalConverter<User, UserDto>() {
		override fun convertTo(
			source: User,
			destinationType: Type<UserDto>,
			mappingContext: MappingContext
		): UserDto {
			return UserDto(
				source.id ?: 0L,
				source.email,
				login = source.login,
				role = source.role.toString(),
				image = source.image?.let { ImageDto.fill(it.name) }
			)
		}

		override fun convertFrom(
			source: UserDto,
			destinationType: Type<User>,
			mappingContext: MappingContext?
		): User {
			return User(
				source.id,
				email = source.email,
				login = source.login,
				role = null,
				createdDate = null,
				password = "",
				isEnabled = true,
				isAccountNonLocked = true
			)
		}
	}
}