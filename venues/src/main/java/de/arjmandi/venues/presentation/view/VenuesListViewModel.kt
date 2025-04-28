package de.arjmandi.venues.presentation.view

import androidx.lifecycle.ViewModel
import de.arjmandi.venues.presentation.model.VenuesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VenuesListViewModel(
	val venuesListContext: VenuesListContext,
) : ViewModel() {
	private val _uiState = MutableStateFlow<VenuesUiState>(VenuesUiState.Loading)
	val uiState: StateFlow<VenuesUiState> = _uiState
}
