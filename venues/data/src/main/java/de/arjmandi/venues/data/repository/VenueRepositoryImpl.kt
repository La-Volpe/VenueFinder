package de.arjmandi.venues.data.repository

import de.arjmandi.venues.data.api.WoltApiService
import de.arjmandi.venues.data.mapper.toDomain
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.repository.VenueRepository

class VenueRepositoryImpl(
    private val api: WoltApiService,
) : VenueRepository {
    override suspend fun getNearbyVenues(location: Location): List<Venue> {
        val response = api.getVenues(location.latitude, location.longitude)
        return response.sections
            .flatMap { it.items }
            .map { it.venue.toDomain() }
    }
}
