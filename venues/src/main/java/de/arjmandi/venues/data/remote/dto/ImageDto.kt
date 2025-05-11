package de.arjmandi.venues.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
	@SerialName("url")
	val url: String,
	@SerialName("blurhash")
	val hash: String,
)
