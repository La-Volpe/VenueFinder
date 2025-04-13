
package de.arjmandi.venues.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.usecase.GetNearbyVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetSupportedCitiesUseCase
import de.arjmandi.venues.presentation.model.LocationListLoadingState
import de.arjmandi.venues.presentation.model.VenuesUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VenuesViewModel(
    private val getNearbyVenues: GetNearbyVenuesUseCase,
    private val getSupportedCitiesUseCase: GetSupportedCitiesUseCase,
    private val defaultCityName: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow<VenuesUiState>(VenuesUiState.Loading)
    val uiState: StateFlow<VenuesUiState> = _uiState

    private val _isLoadingNext = MutableStateFlow(false)
    val isLoadingNext: StateFlow<Boolean> = _isLoadingNext

    private val _currentLocationIndex = MutableStateFlow(0)
    val currentLocationIndex: StateFlow<Int> = _currentLocationIndex

    private val _locationsListState = MutableStateFlow<LocationListLoadingState>(LocationListLoadingState.Loading)
    val locationsListState: StateFlow<LocationListLoadingState> = _locationsListState

    private val _cityChangedState = MutableStateFlow(defaultCityName)
    val cityChangedState: StateFlow<String> = _cityChangedState

    private val _displayedLocation = MutableStateFlow<Location?>(null)
    val displayedLocation: StateFlow<Location?> = _displayedLocation

    val currentLocation: StateFlow<Location?> =
        combine(
            locationsListState,
            currentLocationIndex,
        ) { locationsState, index ->
            if (locationsState is LocationListLoadingState.Success && locationsState.locations.isNotEmpty()) {
                locationsState.locations.getOrNull(index)
            } else {
                null
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            cityChangedState.collectLatest { cityName ->
                loadSupportedCities(cityName)
                resetIndex()
            }
        }

        viewModelScope.launch {
            currentLocation.collectLatest { location ->
                if (location != null) {
                    _uiState.value = VenuesUiState.Loading
                    loadVenues(location)
                }
            }
        }

        observeLocationLooper()
    }

    private fun resetIndex() {
        _currentLocationIndex.value = 0
    }

    private fun loadSupportedCities(selectedCityName: String) {
        try {
            val locationList =
                getSupportedCitiesUseCase()
                    .find { it.displayName.equals(selectedCityName, ignoreCase = true) }
                    ?.coordinates ?: emptyList()

            if (locationList.isEmpty()) {
                throw IllegalArgumentException("City not found or has no locations.")
            }

            _locationsListState.value = LocationListLoadingState.Success(selectedCityName, locationList)
        } catch (e: Exception) {
            _locationsListState.value = LocationListLoadingState.Error(e.message ?: "Failed to load locations.")
        }
    }

    private fun observeLocationLooper() {
        viewModelScope.launch {
            while (true) {
                delay(10_000)
                updateToNextLocation()
            }
        }
    }

    private fun updateToNextLocation() {
        viewModelScope.launch {
            val state = _locationsListState.value
            if (state is LocationListLoadingState.Success && state.locations.isNotEmpty()) {
                val index = _currentLocationIndex.value
                _currentLocationIndex.value = (index + 1) % state.locations.size
            }
        }
    }

    private fun loadVenues(location: Location) {
        _isLoadingNext.value = true
        viewModelScope.launch {
            try {
                val venues = getNearbyVenues(location)
                _uiState.value = VenuesUiState.Success(venues)
                _displayedLocation.value = location
            } catch (e: Exception) {
                _uiState.value = VenuesUiState.Error(e.message ?: "Failed to load venues.")
            } finally {
                _isLoadingNext.value = false
            }
        }
    }
}
