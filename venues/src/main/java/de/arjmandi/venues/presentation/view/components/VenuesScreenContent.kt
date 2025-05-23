package de.arjmandi.venues.presentation.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
	val headerText by remember(uiState) {
		derivedStateOf {
			when (uiState) {
				is VenuesUiState.Success -> {
					"📍 ${currentLocation?.displayName}"
				}
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
			transition.AnimatedContent(
				transitionSpec = {
					fadeIn() + slideInVertically { -40 } togetherWith
						fadeOut() + slideOutVertically { 40 }
				},
			) { targetState ->
				when (targetState) {
					VenuesUiState.Loading -> {
						Box(
							modifier = Modifier.fillMaxSize(),
							contentAlignment = Alignment.TopCenter,
						) {
							ShimmerVenueList()
						}
					}
					is VenuesUiState.Success -> {
						if (targetState.venues.isEmpty()) {
							Box(
								modifier = Modifier.fillMaxSize(),
								contentAlignment = Alignment.Center,
							) {
								Text("No venues found at this location")
							}
						} else {
							AnimatedVenueList(
								venues = targetState.venues,
								favorites = favorites,
								onFavoriteToggle = onFavoriteToggle,
								modifier = Modifier.fillMaxSize(),
							)
						}
					}
					is VenuesUiState.Error -> {
						Box(
							modifier = Modifier.fillMaxSize(),
							contentAlignment = Alignment.Center,
						) {
							Button(onClick = { onRetry() }) {
								Text("Retry")
							}
						}
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimatedVenueList(
	modifier: Modifier = Modifier,
	venues: List<Venue>,
	favorites: Set<String> = emptySet(),
	onFavoriteToggle: (String) -> Unit,
) {
	val listState = rememberLazyListState()

	LazyColumn(
		state = listState,
		modifier = modifier,
		contentPadding = PaddingValues(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		items(
			items = venues,
			key = { it.id },
			contentType = { it },
		) { venue ->
			AnimatedVisibility(
				visible = true,
				enter = fadeIn() + slideInVertically(),
				exit = fadeOut() + slideOutVertically(),
				modifier = Modifier.animateItem(),
			) {
				VenueCard(
					venue = venue,
					isFavorite = venue.id in favorites,
					onFavoriteToggle = onFavoriteToggle,
				)
			}
		}
	}
}
