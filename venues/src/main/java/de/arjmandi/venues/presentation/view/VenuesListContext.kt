package de.arjmandi.venues.presentation.view

import de.arjmandi.venues.domain.usecase.GetFavoritedVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetVenuesForLocationUseCase
import de.arjmandi.venues.domain.usecase.ObserveLocationUpdatesUseCase
import de.arjmandi.venues.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class VenuesListContext(
	private val observeLocationUpdates: ObserveLocationUpdatesUseCase,
	private val getNearbyVenues: GetVenuesForLocationUseCase,
	private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
	private val getFavoritedVenuesUseCase: GetFavoritedVenuesUseCase,
) {
	val locationFlow = observeLocationUpdates()

	suspend fun getVenues(
		lat: Double,
		lon: Double,
	) = getNearbyVenues(lat, lon)

	fun favoritesFlow(): Flow<Set<String>> =
		flow {
			emitAll(getFavoritedVenuesUseCase())
		}

	suspend fun toggleFavorite(id: String) {
		toggleFavoriteUseCase(id)
	}
}
