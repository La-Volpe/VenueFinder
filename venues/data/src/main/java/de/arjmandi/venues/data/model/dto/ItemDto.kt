package de.arjmandi.venues.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    @SerialName("venue")
    val venue: VenueDto,
)
