package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.repository.FavoriteRepository

class ToggleFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(id: String) {
        favoriteRepository.toggleFavorite(id)
    }
}