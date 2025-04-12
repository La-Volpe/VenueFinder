package de.arjmandi.venues.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val displayName: String = "",
)
