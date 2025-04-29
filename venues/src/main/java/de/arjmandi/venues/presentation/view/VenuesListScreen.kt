package de.arjmandi.venues.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.presentation.model.VenuesUiState
import de.arjmandi.venues.presentation.view.components.VenuesScreenContent

@Composable
fun VenuesListScreen(viewModel: VenuesListViewModel) {
	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.getVenues()
	}

	VenuesScreenContent(
		uiState =
		when {
			uiState.isLoading -> VenuesUiState.Loading
			uiState.errorMessage != null -> VenuesUiState.Error(uiState.errorMessage ?: "Unknown error")
			else -> VenuesUiState.Success(uiState.venues)
		},
		currentLocation = uiState.location,
		favorites = uiState.favoriteVenueIds,
		onFavoriteToggle = { viewModel.toggleFavorites(it) },
	)
}
