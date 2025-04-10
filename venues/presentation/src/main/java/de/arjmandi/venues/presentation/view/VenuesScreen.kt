package de.arjmandi.venues.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.presentation.model.VenuesUiState
import de.arjmandi.venues.presentation.view.components.VenueCard
import de.arjmandi.venues.presentation.viewmodel.VenuesViewModel

@Composable
fun VenuesScreen(viewModel: VenuesViewModel) {
    val state by viewModel.uiState.collectAsState()

    when (val uiState = state) {
        is VenuesUiState.Loading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

        is VenuesUiState.Success ->
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 56.dp), // for bottom nav later
            ) {
                items(uiState.venues) { venue ->
                    VenueCard(venue = venue)
                }
            }

        is VenuesUiState.Error ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = uiState.message)
            }
    }
}

@Preview(showBackground = true)
@Composable
fun VenuesScreenPreview() {
    val venues =
        listOf(
            Venue(
                id = "67c571f5dafd795cde3f22e1",
                name = "Hesburger Helsinki Pasila Tripla",
                shortDescription = "Herkulliset hampurilaiset & tortillat",
                imageUrl = "https://imageproxy.wolt.com/assets/67ea79d8e3aca1debaea9cf4",
            ),
            Venue(
                id = "67039d617b34437fa839f85b",
                name = "Burger King Citycenter",
                shortDescription = "Liekillä grillatut hampurilaiset",
                imageUrl = "https://imageproxy.wolt.com/assets/67335f048c35d92f799b8a8d",
            ),
            Venue(
                id = "561e29316d475308275d6854",
                name = "Subway Iso Roobertinkatu",
                shortDescription = "Parhaat hetket ovat edessä.",
                imageUrl = "https://imageproxy.wolt.com/assets/67332fbe827209684f08e384",
            ),
            Venue(
                id = "6411b53cc19783bc82b1d7cc",
                name = "Cafe Korvari",
                shortDescription = "Breakfast in Töölö or freshly baked cinnamon roll, also home-delivered!",
                imageUrl = "https://imageproxy.wolt.com/assets/67321485a94945236d40b6ff",
            ),
            Venue(
                id = "661933b4a8a2d509c33d0f53",
                name = "Pizzakuningas Pasila",
                shortDescription = "Kuningas pizzaa ja kebabia!",
                imageUrl = "https://imageproxy.wolt.com/assets/6735bf10644b107941dc6fc2",
            ),
            Venue(
                id = "66262b2273278067c6a544fc",
                name = "King's Pizza",
                shortDescription = "Vönerit ja vegaanipizzat",
                imageUrl = "https://imageproxy.wolt.com/assets/673330473bdb662b380907f3",
            ),
        )
    MaterialTheme {
        LazyColumn {
            items(venues) {
                VenueCard(it)
            }
        }
    }
}
