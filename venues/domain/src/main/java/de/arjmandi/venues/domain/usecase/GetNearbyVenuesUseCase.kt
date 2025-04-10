package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.repository.VenueRepository

class GetNearbyVenuesUseCase(
    private val repository: VenueRepository,
) {
    suspend operator fun invoke(location: Location): List<Venue> = repository.getNearbyVenues(location).take(15)
}
