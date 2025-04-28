package de.arjmandi.venues.presentation.model

import de.arjmandi.venues.domain.model.Venue

sealed interface VenuesUiState {
    data object Loading : VenuesUiState

    data class Success(
        val venues: List<Venue>,
    ) : VenuesUiState

    data class Error(
        val message: String,
    ) : VenuesUiState
}
