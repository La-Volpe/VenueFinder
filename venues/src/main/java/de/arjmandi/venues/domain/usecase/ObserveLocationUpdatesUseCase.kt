package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class ObserveLocationUpdatesUseCase(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<Pair<Double, Double>> = locationRepository.locationFlow
}