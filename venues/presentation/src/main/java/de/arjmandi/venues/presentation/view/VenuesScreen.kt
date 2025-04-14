
package de.arjmandi.venues.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import de.arjmandi.venues.presentation.view.components.VenuesScreenContent
import de.arjmandi.venues.presentation.viewmodel.VenuesViewModel

@Composable
fun VenuesScreen(viewModel: VenuesViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    VenuesScreenContent(
        uiState = uiState,
        currentLocation = currentLocation,
    )
}
