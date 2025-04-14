package de.arjmandi.venues.data.repository

import de.arjmandi.venues.data.database.FavoriteVenueDao
import de.arjmandi.venues.data.model.entities.FavoriteVenueEntity
import de.arjmandi.venues.domain.repository.FavoriteVenueRepository

class FavoriteVenueRepositoryImpl(
    private val dao: FavoriteVenueDao,
) : FavoriteVenueRepository {
    override suspend fun isFavorite(venueId: String) = dao.isFavorite(venueId)

    override suspend fun toggleFavorite(venueId: String) {
        if (dao.isFavorite(venueId)) {
            dao.removeFavorite(FavoriteVenueEntity(venueId))
        } else {
            dao.addFavorite(FavoriteVenueEntity(venueId))
        }
    }

    override suspend fun getFavorites(): Set<String> = dao.getAll().map { it.venueId }.toSet()
}
