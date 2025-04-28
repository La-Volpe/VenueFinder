package de.arjmandi.venues.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.presentation.model.VenuesUiState
import de.arjmandi.venues.util.BackendLoadingException
import de.arjmandi.venues.util.NetworkLoadingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VenuesListViewModel(
	val venuesListContext: VenuesListContext,
) : ViewModel() {
	private val _uiState = MutableStateFlow<VenuesUiState>(VenuesUiState.Loading)
	val uiState: StateFlow<VenuesUiState> = _uiState

	private val _currentLocation = venuesListContext.currentLocation
	val currentLocation: StateFlow<Location> = _currentLocation
	private val _favoriteList = venuesListContext.favoriteVenuesState
	val favoriteList: StateFlow<Set<String>> = _favoriteList

	fun getVenues() {
		viewModelScope.launch(Dispatchers.IO) {
			venuesListContext.observeLocationChanges()
			venuesListContext.observeFavorites()
		}
		startCollecting()
	}

	private fun startCollecting() {
		viewModelScope.launch {
			venuesListContext.venuesListState.collectLatest { venues ->
				try {
					_uiState.value = VenuesUiState.Success(venues)
				} catch (e: Exception) {
					when (e) {
						is BackendLoadingException -> _uiState.value = VenuesUiState.Error(e.message)
						is NetworkLoadingException -> _uiState.value = VenuesUiState.Error(e.message)
						else -> _uiState.value = VenuesUiState.Error("Unknown Error: ${e.message}")
					}
				}
			}
			venuesListContext.currentLocation.collectLatest {
				_uiState.value = VenuesUiState.Loading
			}
			_favoriteList.collectLatest {
			}
		}
	}

	fun toggleFavorites(id: String) {
		viewModelScope.launch {
			venuesListContext.toggleFavorite(id)
		}
	}

	override fun onCleared() {
		super.onCleared()
		viewModelScope.cancel()
	}
}
