package de.arjmandi.venues.presentation.view

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.usecase.GetFavoritedVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetVenuesForLocationUseCase
import de.arjmandi.venues.domain.usecase.ObserveLocationUpdatesUseCase
import de.arjmandi.venues.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class VenuesListContextTest {
	private val locationsChange = mockk<ObserveLocationUpdatesUseCase>()
	private val getNearbyVenues = mockk<GetVenuesForLocationUseCase>()
	private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()
	private val getFavoritedVenuesUseCase = mockk<GetFavoritedVenuesUseCase>()

	private lateinit var context: VenuesListContext

	@Before
	fun setup() {
		context =
			VenuesListContext(
				locationsChange = locationsChange,
				getNearbyVenues = getNearbyVenues,
				toggleFavorite = toggleFavoriteUseCase,
				getFavoritedVenues = getFavoritedVenuesUseCase,
			)
	}

	@Test
	fun observeLocationChanges_withLocationAndVenues_updatesLocationAndVenuesState() =
		runTest {
			// Arrange
			val targetIndex = 1
			val coord = Location.coordinates[targetIndex]
			val venue = Venue("v1", "Name", "Desc", "url")
			coEvery { locationsChange.invoke() } returns flowOf(coord.latitude to coord.longitude)
			coEvery { getNearbyVenues.invoke(coord.latitude, coord.longitude) } returns flowOf(listOf(venue))

			// Act
			val job = launch { context.observeLocationChanges() }
			advanceUntilIdle()

			// Assert
			assertEquals(coord, context.currentLocation.value)
			assertEquals(listOf(venue), context.venuesListState.value)

			job.cancel()
		}

	@Test
	fun observeFavorites_withFavorites_updatesFavoriteState() =
		runTest {
			// Arrange
			val favorites = setOf("v1")
			coEvery { getFavoritedVenuesUseCase.invoke() } returns flowOf(favorites)

			// Act
			val job = launch { context.observeFavorites() }
			advanceUntilIdle()

			// Assert
			assertEquals(favorites, context.favoriteVenuesState.value)

			job.cancel()
		}

	@Test
	fun toggleFavorite_withValidVenueId_invokesUseCase() =
		runTest {
			// Arrange
			val id = "v1"
			coEvery { toggleFavoriteUseCase.invoke(id) } returns Unit

			// Act
			context.toggleFavorite(id)

			// Assert
			coVerify(exactly = 1) { toggleFavoriteUseCase.invoke(id) }
		}
}
