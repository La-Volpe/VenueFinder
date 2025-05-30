package de.arjmandi.venues.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
	@SerialName("image")
	val image: ImageDto,
	@SerialName("venue")
	val venue: VenueDto? = null,
)
