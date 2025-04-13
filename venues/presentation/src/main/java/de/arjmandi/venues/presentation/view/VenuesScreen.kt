
package de.arjmandi.venues.presentation.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.arjmandi.venues.presentation.model.VenuesUiState
import de.arjmandi.venues.presentation.view.components.VenueCard
import de.arjmandi.venues.presentation.viewmodel.VenuesViewModel
import kotlinx.coroutines.delay

@Composable
fun VenuesScreen(viewModel: VenuesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoadingNext by viewModel.isLoadingNext.collectAsState()
    val displayedLocation by viewModel.displayedLocation.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = uiState is VenuesUiState.Loading,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(56.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 6.dp,
            )
        }

        when (uiState) {
            is VenuesUiState.Success -> {
                val successState = uiState as VenuesUiState.Success

                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = 56.dp),
                ) {
                    item {
                        AnimatedContent(
                            targetState = displayedLocation?.displayName.orEmpty(),
                            transitionSpec = {
                                (slideInVertically { -it } + fadeIn()) togetherWith
                                    (slideOutVertically { it } + fadeOut())
                            },
                            label = "LocationHeader",
                        ) { name ->
                            if (name.isNotBlank()) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier =
                                        Modifier.padding(
                                            top = 32.dp,
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = 12.dp,
                                        ),
                                )
                            }
                        }
                    }

                    itemsIndexed(successState.venues) { index, venue ->
                        var isVisible by remember(venue.id) { mutableStateOf(false) }

                        LaunchedEffect(Unit) {
                            delay(index * 80L)
                            isVisible = true
                        }

                        AnimatedVisibility(
                            visible = isVisible,
                            enter = slideInVertically { it / 2 } + fadeIn(tween(300)),
                        ) {
                            VenueCard(venue = venue)
                        }
                    }
                }

                if (isLoadingNext) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(56.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 6.dp,
                        )
                    }
                }
            }

            is VenuesUiState.Error -> {
                val errorState = uiState as VenuesUiState.Error
                Text(
                    text = errorState.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp),
                )
            }

            else -> Unit
        }
    }
}
