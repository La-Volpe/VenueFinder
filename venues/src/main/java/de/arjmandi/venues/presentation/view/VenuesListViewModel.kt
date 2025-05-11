package de.arjmandi.venues.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.presentation.model.VenuesListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VenuesListViewModel(
	private val context: VenuesListContext,
) : ViewModel() {
	private val _uiState = MutableStateFlow(VenuesListUiState())
	val uiState: StateFlow<VenuesListUiState> = _uiState.asStateFlow()
	private val _isPaused = MutableStateFlow(false)
	val isPaused: StateFlow<Boolean> = _isPaused

	private lateinit var job: Job

	fun getVenues() {
		observeLocationAndFavorites()
	}

	private fun observeLocationAndFavorites() {
		job = viewModelScope.launch {
			context
				.locationFlow
				.cancellable()
				.filter { !isPaused.value }
				.collectLatest { location ->
					_uiState.update { it.copy(location = location, isLoading = true) }
					fetchVenues(location)
				}
		}

		viewModelScope.launch {
			context.favoritesFlow().collectLatest { favorites ->
				_uiState.update { it.copy(favoriteVenueIds = favorites) }
			}
		}
	}

	private suspend fun fetchVenues(location: Location) {
		context
			.getVenues(location.latitude, location.longitude)
			.onEach { venues ->
				_uiState.update { it.copy(venues = venues, isLoading = false, errorMessage = null) }
			}.catch { e ->
				_isPaused.update { true }
				_uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
				job.cancel()
			}.collect {}
	}

	fun toggleFavorites(id: String) {
		viewModelScope.launch {
			context.toggleFavorite(id)
		}
	}

	fun retry(): () -> Unit = {
		_isPaused.value = false
		observeLocationAndFavorites()
	}
}
