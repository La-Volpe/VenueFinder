package de.arjmandi.venues.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.usecase.GetNearbyVenuesUseCase
import de.arjmandi.venues.presentation.model.VenuesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VenuesViewModel(
    private val getNearbyVenues: GetNearbyVenuesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<VenuesUiState>(VenuesUiState.Loading)
    val uiState: StateFlow<VenuesUiState> = _uiState

    fun loadVenues() {
        val location = Location(60.169418, 24.931618)
        _uiState.value = VenuesUiState.Loading
        viewModelScope.launch {
            try {
                val venues = getNearbyVenues(location)
                _uiState.value = VenuesUiState.Success(venues)
            } catch (e: Exception) {
                _uiState.value = VenuesUiState.Error("Failed to load venues")
            }
        }
    }
}
