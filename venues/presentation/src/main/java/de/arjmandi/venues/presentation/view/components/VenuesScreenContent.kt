package de.arjmandi.venues.presentation.view.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
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

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun VenuesScreenContent(
    uiState: VenuesUiState,
    currentLocation: Location?,
    modifier: Modifier = Modifier,
) {
    val locationName by remember(currentLocation) {
        derivedStateOf {
            when (uiState) {
                is VenuesUiState.Success -> {
                    "📍 $currentLocation"
                }
                is VenuesUiState.Error -> "📍 Error location"
                VenuesUiState.Loading -> "📍 Loading location..."
            }
        }
    }

    // Animation for content transitions
    val transition = updateTransition(targetState = uiState, label = "contentTransition")

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = locationName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
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
                        Crossfade(
                            targetState = Unit,
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopCenter,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is VenuesUiState.Success -> {
                        if (targetState.venues.isEmpty()) {
                            Crossfade(
                                targetState = Unit,
                                modifier = Modifier.align(Alignment.Center),
                            ) {
                                Text("No venues found at this location")
                            }
                        } else {
                            AnimatedVenueList(
                                venues = targetState.venues,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                    is VenuesUiState.Error -> {
                        Crossfade(
                            targetState = Unit,
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            Text(
                                text = targetState.message,
                                color = MaterialTheme.colorScheme.error,
                            )
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
    venues: List<Venue>,
    modifier: Modifier = Modifier,
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
            contentType = { it }, // Helps with item animations
        ) { venue ->
            // Each item gets its own animation
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier.animateItemPlacement(),
            ) {
                VenueCard(
                    venue = venue,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
