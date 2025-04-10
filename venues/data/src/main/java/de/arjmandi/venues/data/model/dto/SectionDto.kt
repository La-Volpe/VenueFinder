package de.arjmandi.venues.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SectionDto(
    @SerialName("items")
    val items: List<ItemDto>,
)
