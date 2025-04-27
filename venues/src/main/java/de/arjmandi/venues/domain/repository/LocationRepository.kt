package de.arjmandi.venues.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    val locationFlow: Flow<Pair<Double, Double>>
}