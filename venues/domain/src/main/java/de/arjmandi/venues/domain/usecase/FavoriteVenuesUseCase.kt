package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.repository.FavoriteVenueRepository

class FavoriteVenuesUseCase(
    private val repository: FavoriteVenueRepository,
) {
    suspend fun getFavorites() = repository.getFavorites()

    suspend fun isFavorite(venueId: String) = repository.isFavorite(venueId)

    suspend fun toggleFavorite(venueId: String) = repository.toggleFavorite(venueId)
}
