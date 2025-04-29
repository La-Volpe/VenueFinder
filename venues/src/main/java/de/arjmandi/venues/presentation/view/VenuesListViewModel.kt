package de.arjmandi.venues.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.presentation.model.VenuesListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VenuesListViewModel(
	private val context: VenuesListContext,
) : ViewModel() {
	private val _uiState = MutableStateFlow(VenuesListUiState())
	val uiState: StateFlow<VenuesListUiState> = _uiState.asStateFlow()

	fun getVenues() {
		observeLocationAndFavorites()
	}

	private fun observeLocationAndFavorites() {
		viewModelScope.launch {
			combine(
				context.locationFlow,
				context.favoritesFlow(),
			) { location, favorites ->
				location to favorites
			}.collectLatest { (location, favorites) ->
				_uiState.update { it.copy(location = location, favoriteVenueIds = favorites, isLoading = true) }
				fetchVenues(location)
			}
		}
	}

	private suspend fun fetchVenues(location: Location) {
		context
			.getVenues(location.latitude, location.longitude)
			.onEach { venues ->
				_uiState.update { it.copy(venues = venues, isLoading = false, errorMessage = null) }
			}.catch { e ->
				_uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
			}.collect {}
	}

	fun toggleFavorites(id: String) {
		viewModelScope.launch {
			context.toggleFavorite(id)
		}
	}
}
