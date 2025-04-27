package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.repository.FavoriteRepository
import de.arjmandi.venues.domain.repository.VenueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.take

class GetVenuesForLocationUseCase(
    private val venueRepository: VenueRepository,
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Flow<List<Venue>> {
        val venuesFlow = venueRepository.getVenues(lat, lon)
        return venuesFlow
            .take(15)
    }
}