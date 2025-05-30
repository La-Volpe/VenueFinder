package de.arjmandi.venues.data.constants

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class LocationRepositoryImpl(
	coroutineScope: CoroutineScope,
) : LocationRepository {
	private val _locationFlow: SharedFlow<Location> =
		callbackFlow {
			var index = 0
			val job =
				launch {
					while (true) {
						trySend(Location.coordinates[index])
						delay(10_000)
						index = (index + 1) % Location.coordinates.size
					}
				}
			awaitClose { job.cancel() }
		}.shareIn(
			scope = coroutineScope,
			started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 0),
			replay = 1,
		)

	override val locationFlow: SharedFlow<Location> = _locationFlow
}
