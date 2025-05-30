package de.arjmandi.venues.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.arjmandi.venues.domain.model.Venue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedVenueList(
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
