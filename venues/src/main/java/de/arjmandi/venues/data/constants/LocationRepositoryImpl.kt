package de.arjmandi.venues.data.constants

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationRepositoryImpl : LocationRepository {

	override val locationFlow: Flow<Location> =
		flow {
			var index = 0
			while (true) {
				emit(Location.coordinates[index])
				delay(10_000)
				index = (index + 1) % Location.coordinates.size
			}
		}
}
