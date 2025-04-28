package de.arjmandi.venues.presentation.model

import de.arjmandi.venues.domain.model.Venue

data class VenuesListUiState(
	val location: Pair<Double, Double>? = null,
	val venues: List<Venue> = emptyList(),
	val favoriteVenueIds: Set<String> = emptySet(),
	val isLoading: Boolean = true,
	val errorMessage: String? = null,
)
