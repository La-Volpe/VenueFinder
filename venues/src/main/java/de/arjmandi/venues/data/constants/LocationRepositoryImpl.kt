package de.arjmandi.venues.data.constants

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationRepositoryImpl : LocationRepository {
	private val locations =
		Location.coordinates.map {
			it.latitude to it.longitude
		}

	override val locationFlow: Flow<Pair<Double, Double>> =
		flow {
			var index = 0
			while (true) {
				emit(locations[index])
				delay(10_000)
				index = (index + 1) % locations.size
			}
		}
}
