package de.arjmandi.venues.presentation.view

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.usecase.GetFavoritedVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetVenuesForLocationUseCase
import de.arjmandi.venues.domain.usecase.ObserveLocationUpdatesUseCase
import de.arjmandi.venues.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class VenuesListContextTest {

	private lateinit var context: VenuesListContext
	private val observeLocationUpdates: ObserveLocationUpdatesUseCase = mockk()
	private val getNearbyVenues: GetVenuesForLocationUseCase = mockk()
	private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
	private val getFavoritedVenuesUseCase: GetFavoritedVenuesUseCase = mockk()

	@Before
	fun setup() {
		// Configure the ObserveLocationUpdatesUseCase to return a flow of locations
		val expectedLocation = Location(60.169418, 24.931618, "Kamppi Chapel of Silence")
		every { observeLocationUpdates() } returns flowOf(expectedLocation)

		// Configure the ToggleFavoriteUseCase to handle any string input
		coEvery { toggleFavoriteUseCase(any()) } just runs

		context = VenuesListContext(
			observeLocationUpdates = observeLocationUpdates,
			getNearbyVenues = getNearbyVenues,
			toggleFavoriteUseCase = toggleFavoriteUseCase,
			getFavoritedVenuesUseCase = getFavoritedVenuesUseCase,
		)
	}

	@Test
	fun `locationFlow should return flow from observeLocationUpdates use case`() = runTest {
		// Given
		val expectedLocation = Location(60.169418, 24.931618, "Kamppi Chapel of Silence")
		every { observeLocationUpdates() } returns flowOf(expectedLocation)

		// When
		val result = context.locationFlow

		// Then
		result.collect { location ->
			assertEquals(expectedLocation, location)
		}
	}

	@Test
	fun `getVenues should delegate to getNearbyVenues use case`() = runTest {
		// Given
		val lat = 60.169418
		val lon = 24.931618
		val expectedVenues = listOf(
			Venue("1", "Venue 1", "Description 1", "image1", false),
			Venue("2", "Venue 2", "Description 2", "image2", false),
		)
		coEvery { getNearbyVenues(lat, lon) } returns flowOf(expectedVenues)

		// When
		val result = context.getVenues(lat, lon)

		// Then
		result.collect { venues ->
			assertEquals(expectedVenues, venues)
		}
	}

	@Test
	fun `favoritesFlow should delegate to getFavoritedVenuesUseCase`() = runTest {
		// Given
		val expectedFavorites = setOf("venue1", "venue2")
		coEvery { getFavoritedVenuesUseCase() } returns flowOf(expectedFavorites)

		// When
		val result = context.favoritesFlow()

		// Then
		result.collect { favorites ->
			assertEquals(expectedFavorites, favorites)
		}
	}

	@Test
	fun `toggleFavorite should delegate to toggleFavoriteUseCase`() = runTest {
		// Given
		val venueId = "venue123"
		coEvery { toggleFavoriteUseCase(venueId) } just runs

		// When
		context.toggleFavorite(venueId)

		// Then
		coVerify { toggleFavoriteUseCase(venueId) }
	}

	@Test
	fun `getVenues should handle invalid latitude and longitude inputs`() = runTest {
		// Given
		val invalidLat = 91.0 // Invalid latitude value
		val invalidLon = 181.0 // Invalid longitude value

		coEvery { getNearbyVenues(invalidLat, invalidLon) } returns flowOf(emptyList())

		// When
		val venues = context.getVenues(invalidLat, invalidLon)

		// Then
		venues.collect { result ->
			assertTrue(result.isEmpty())
		}
	}

	@Test
	fun `favoritesFlow should handle empty or null data`() = runTest {
		// Given
		coEvery { getFavoritedVenuesUseCase() } returns flowOf(setOf())

		// When
		val result = context.favoritesFlow()

		// Then
		result.collect { favorites ->
			assertTrue(favorites.isEmpty())
		}
	}
}
