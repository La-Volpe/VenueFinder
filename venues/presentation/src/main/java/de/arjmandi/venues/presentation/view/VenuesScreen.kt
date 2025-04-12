package de.arjmandi.venues.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.arjmandi.venues.presentation.model.VenuesUiState
import de.arjmandi.venues.presentation.view.components.VenueCard
import de.arjmandi.venues.presentation.viewmodel.VenuesViewModel

@Composable
fun VenuesScreen(viewModel: VenuesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoadingNext by viewModel.isLoadingNext.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (uiState) {
            is VenuesUiState.Loading -> {
                // Loading state UI with fade-in animation
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(56.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 6.dp,
                    )
                }
            }

            is VenuesUiState.Success -> {
                val successState = uiState as VenuesUiState.Success
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = 56.dp),
                    // bottom nav space
                ) {
                    item {
                        currentLocation?.displayName?.takeIf { it.isNotBlank() }?.let {
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                            ) {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier =
                                        Modifier
                                            .padding(
                                                top = 32.dp,
                                                start = 16.dp,
                                                end = 16.dp,
                                                bottom = 12.dp,
                                            ),
                                )
                            }
                        }
                    }

                    // Add items to the list with animations
                    successState.venues.forEachIndexed { index, venue ->
                        item {
                            AnimatedVisibility(
                                visible = true,
                                enter =
                                    slideInVertically(
                                        initialOffsetY = { it + (index * 50) }, // Staggered entrance
                                        animationSpec = tween(durationMillis = 300),
                                    ) +
                                        fadeIn(
                                            animationSpec = tween(durationMillis = 300),
                                        ),
                            ) {
                                VenueCard(venue = venue)
                            }
                        }
                    }
                }

                // Small overlay for loading the next list
                if (isLoadingNext) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                    ) {
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
            }

            is VenuesUiState.Error -> {
                val errorState = uiState as VenuesUiState.Error
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                ) {
                    Text(
                        text = errorState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}
