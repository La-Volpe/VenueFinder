package de.arjmandi.venues.domain.repository

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue

interface VenueRepository {
    suspend fun getNearbyVenues(location: Location): List<Venue>
}
