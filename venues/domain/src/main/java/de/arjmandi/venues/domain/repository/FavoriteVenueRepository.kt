package de.arjmandi.venues.domain.repository

interface FavoriteVenueRepository {
    suspend fun isFavorite(venueId: String): Boolean

    suspend fun toggleFavorite(venueId: String)

    suspend fun getFavorites(): Set<String>
}
