package de.arjmandi.venues.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VenueDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("short_description")
    val shortDescription: String? = "",
    @SerialName("brand_image")
    val brandImage: ImageDto? = null,
)
