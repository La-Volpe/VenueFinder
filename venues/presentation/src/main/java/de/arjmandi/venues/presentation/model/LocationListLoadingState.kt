package de.arjmandi.venues.presentation.model

import de.arjmandi.venues.domain.model.Location

sealed interface LocationListLoadingState {
    data object Loading : LocationListLoadingState

    data class Loaded(
        val locations: List<Location>,
    ) : LocationListLoadingState

    data class Error(
        val message: String,
    ) : LocationListLoadingState
}
