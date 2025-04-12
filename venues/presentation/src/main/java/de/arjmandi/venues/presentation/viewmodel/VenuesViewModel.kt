package de.arjmandi.venues.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.usecase.GetNearbyVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetSupportedCitiesUseCase
import de.arjmandi.venues.presentation.model.VenuesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VenuesViewModel(
    private val getNearbyVenues: GetNearbyVenuesUseCase,
    private val getSupportedCitiesUseCase: GetSupportedCitiesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<VenuesUiState>(VenuesUiState.Loading)
    val uiState: StateFlow<VenuesUiState> = _uiState

    fun loadVenues(selectedCityName: String = "vienna") {
        _uiState.value = VenuesUiState.Loading
        viewModelScope.launch {
            try {
                val venues = getNearbyVenues(loadSupportedCities(selectedCityName).first())
                _uiState.value = VenuesUiState.Success(venues)
            } catch (e: Exception) {
                _uiState.value = VenuesUiState.Error(e.message ?: "Failed to load venues.")
            }
        }
    }

    private fun loadSupportedCities(selectedCityName: String): List<Location> {
        val locationList =
            getSupportedCitiesUseCase()
                .find {
                    it.displayName.equals(selectedCityName, ignoreCase = true)
                }?.coordinates ?: emptyList()
        if (locationList.isEmpty()) {
            throw IllegalArgumentException("City not found")
        }
        return locationList
    }
}
