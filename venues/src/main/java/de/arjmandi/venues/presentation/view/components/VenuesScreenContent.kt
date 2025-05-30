package de.arjmandi.venues.presentation.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.presentation.model.VenuesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenuesScreenContent(
	uiState: VenuesUiState,
	currentLocation: Location?,
	modifier: Modifier = Modifier,
	favorites: Set<String> = emptySet(),
	onRetry: () -> Unit = {},
	onFavoriteToggle: (String) -> Unit = {},
) {
	var lastSuccessfulVenues by remember { mutableStateOf<List<Venue>?>(null) }

	LaunchedEffect(uiState) {
		if (uiState is VenuesUiState.Success) {
			lastSuccessfulVenues = uiState.venues
		}
	}

	val headerText by remember(uiState) {
		derivedStateOf {
			when (uiState) {
				is VenuesUiState.Success -> "📍 ${currentLocation?.displayName}"
				is VenuesUiState.Error -> "📍 Error Loading location"
				VenuesUiState.Loading -> "📍 Loading location..."
			}
		}
	}

	val transition = updateTransition(targetState = uiState, label = "contentTransition")

	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = {
					when (uiState) {
						is VenuesUiState.Loading -> FlashingFirstCharacterText(headerText)
						is VenuesUiState.Error -> Text("\uD83D\uDD34 Error Loading Venues!")
						is VenuesUiState.Success -> {
							Text(
								text = headerText,
								style = MaterialTheme.typography.titleLarge,
								maxLines = 1,
								overflow = TextOverflow.Ellipsis,
							)
						}
					}
				},
			)
		},
	) { innerPadding ->
		Box(
			modifier =
				Modifier
					.padding(innerPadding)
					.fillMaxSize(),
		) {
			// Always show the last successful venues if they exist
			lastSuccessfulVenues?.let { venues ->
				AnimatedVenueList(
					venues = venues,
					favorites = favorites,
					onFavoriteToggle = onFavoriteToggle,
					modifier = Modifier.fillMaxSize(),
				)
			}

			// Overlay the loading or error state on top
			Box(modifier = Modifier.fillMaxSize()) {
				transition.AnimatedContent(
					transitionSpec = {
						fadeIn() + slideInVertically { -40 } togetherWith
							fadeOut() + slideOutVertically { 40 }
					},
				) { targetState ->
					when (targetState) {
						VenuesUiState.Loading -> {
							if (lastSuccessfulVenues == null) {
								// Only show shimmer if we have no content yet
								Box(
									modifier = Modifier.fillMaxSize(),
									contentAlignment = Alignment.TopCenter,
								) {
									ShimmerVenueList()
								}
							} else {
								// During updates, we already show the last content
								Box {}
							}
						}
						is VenuesUiState.Success -> {
							// Content is already shown via lastSuccessfulVenues
							Box {}
						}
						is VenuesUiState.Error -> {
							Box(
								modifier = Modifier.fillMaxSize(),
								contentAlignment = Alignment.Center,
							) {
								Column(horizontalAlignment = Alignment.CenterHorizontally) {
									Button(onClick = onRetry) {
										Text("Retry")
									}
									// Show shimmer when retrying after error if we have no content
									if (lastSuccessfulVenues == null) {
										ShimmerVenueList()
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
