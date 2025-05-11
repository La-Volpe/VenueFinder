package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.repository.VenueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetVenuesForLocationUseCase(
	private val venueRepository: VenueRepository,
) {
	suspend operator fun invoke(
		lat: Double,
		lon: Double,
	): Flow<List<Venue>> =
		venueRepository.getVenues(lat, lon).map { venues ->
			venues.take(15)
		}
}
