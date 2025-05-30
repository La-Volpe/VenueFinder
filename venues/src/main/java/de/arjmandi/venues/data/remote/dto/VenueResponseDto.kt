package de.arjmandi.venues.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VenueResponseDto(
	@SerialName("sections")
	val sections: List<SectionDto>,
)
