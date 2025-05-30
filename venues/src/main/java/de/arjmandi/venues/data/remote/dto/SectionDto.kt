package de.arjmandi.venues.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SectionDto(
	@SerialName("name")
	val name: String,
	@SerialName("items")
	val items: List<ItemDto>? = null,
)
