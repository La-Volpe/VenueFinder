package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritedVenuesUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(): Flow<Set<String>> {
        return favoriteRepository.getAllFavorite()
    }
}