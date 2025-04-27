package de.arjmandi.venues.domain.repository

import de.arjmandi.venues.domain.model.Venue
import kotlinx.coroutines.flow.Flow

interface VenueRepository {
    fun getVenues(lat: Double, lon: Double): Flow<List<Venue>>
}