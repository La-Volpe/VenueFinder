package de.arjmandi.venues.presentation.view

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.usecase.GetFavoritedVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetVenuesForLocationUseCase
import de.arjmandi.venues.domain.usecase.ObserveLocationUpdatesUseCase
import de.arjmandi.venues.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class VenuesListContext(
	private val locationsChange: ObserveLocationUpdatesUseCase,
	private val getNearbyVenues: GetVenuesForLocationUseCase,
	private val toggleFavorite: ToggleFavoriteUseCase,
	private val getFavoritedVenues: GetFavoritedVenuesUseCase,
) {
	// 🟢 Location
	private val currentLocationState: MutableStateFlow<Location> = MutableStateFlow(Location.coordinates[0])
	val currentLocation: StateFlow<Location> = currentLocationState

	// 🟢 Venues
	private val _venuesListState: MutableStateFlow<List<Venue>> = MutableStateFlow(emptyList())
	val venuesListState: StateFlow<List<Venue>> = _venuesListState

	// 🟢 Favorite Venues
	private val _favoriteVenuesState: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
	val favoriteVenuesState: StateFlow<Set<String>> = _favoriteVenuesState

	suspend fun toggleFavorite(venueId: String) {
		toggleFavorite.invoke(venueId)
	}

	private suspend fun getVenuesList(location: Pair<Double, Double>) {
		getNearbyVenues.invoke(location.first, location.second).collectLatest { venues ->
			_venuesListState.value = venues
		}
	}

	suspend fun observeLocationChanges() {
		locationsChange.invoke().collectLatest { location ->
			getVenuesList(location)
			currentLocationState.value =
				Location.coordinates.first {
					location.first == it.latitude && location.second == it.longitude
				}
		}
	}

	suspend fun observeFavorites() {
		getFavoritedVenues.invoke().collectLatest {
			_favoriteVenuesState.value = it
		}
	}
}
