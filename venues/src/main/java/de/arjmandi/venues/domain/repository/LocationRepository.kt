package de.arjmandi.venues.domain.repository

import de.arjmandi.venues.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
	val locationFlow: Flow<Location>
}
