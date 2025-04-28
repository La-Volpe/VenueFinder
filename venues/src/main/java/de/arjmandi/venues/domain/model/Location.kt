package de.arjmandi.venues.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val displayName: String = "",
) {
    companion object {
        val coordinates = listOf(
        Location(60.169418, 24.931618, "Kamppi Chapel of Silence"),
        Location(60.169818, 24.932906, "Narinkka Square"),
        Location(60.170005, 24.935105, "Kamppi Shopping Centre"),
        Location(60.169108, 24.936210, "Tennispalatsi (Finnkino)"),
        Location(60.168355, 24.934869, "Helsinki Bus Station"),
        Location(60.167560, 24.932562, "Helsinki Music Centre"),
        Location(60.168254, 24.931532, "Sanomatalo"),
        Location(60.169012, 24.930341, "Kiasma Museum of Contemporary Art"),
        Location(60.170085, 24.929569, "Helsinki Central Railway Station"),
        )
    }
}