@file:OptIn(ExperimentalCoroutinesApi::class)

package de.arjmandi.venues.presentation.viewmodel

import app.cash.turbine.test
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.usecase.FavoriteVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetNearbyVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetSupportedCitiesUseCase
import de.arjmandi.venues.presentation.model.LocationListLoadingState
import de.arjmandi.venues.presentation.model.VenuesUiState
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class VenuesViewModelTest {
    private val getNearbyVenues: GetNearbyVenuesUseCase = mockk()
    private val getSupportedCities: GetSupportedCitiesUseCase = mockk()
    private val favoriteVenuesUseCase: FavoriteVenuesUseCase = mockk()

    private lateinit var viewModel: VenuesViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testCity = "Helsinki"
    private val testLocation = Location(1.0, 2.0, "loc1")
    private val testVenues =
        listOf(
            Venue(
                id = "67c571f5dafd795cde3f22e1",
                name = "Hesburger Helsinki Pasila Tripla",
                shortDescription = "Herkulliset hampurilaiset & tortillat",
                imageUrl = "https://imageproxy.wolt.com/assets/67ea79d8e3aca1debaea9cf4",
            ),
        )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getSupportedCities() } returns GetSupportedCitiesUseCase().invoke()

        coEvery { getNearbyVenues(any()) } returns testVenues
        coEvery { favoriteVenuesUseCase.getFavorites() } returns setOf("v1")
        coJustRun { favoriteVenuesUseCase.toggleFavorite(any()) }

        viewModel =
            VenuesViewModel(
                getNearbyVenues,
                getSupportedCities,
                favoriteVenuesUseCase,
                defaultCityName = testCity,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should emit loading and then supported cities`() =
        runTest {
            viewModel.locationsListState.test {
                assert(awaitItem() is LocationListLoadingState.Loading)
                val state = awaitItem()
                assert(state is LocationListLoadingState.Success)
                assert((state as LocationListLoadingState.Success).locations.contains(testLocation))
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `current location should emit valid location after init`() =
        runTest {
            viewModel.currentLocation.test {
                skipItems(1) // initial null
                val location = awaitItem()
                assert(location == testLocation)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `uiState should emit Loading then Success when venues load`() =
        runTest {
            viewModel.uiState.test {
                assert(awaitItem() is VenuesUiState.Loading)
                val state = awaitItem()
                assert(state is VenuesUiState.Success)
                assert((state as VenuesUiState.Success).venues == testVenues)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `loadFavorites should update favoriteVenueIds`() =
        runTest {
            viewModel.favoriteVenueIds.test {
                assert(awaitItem().isEmpty())
                viewModel.loadFavorites()
                assert(awaitItem() == setOf("v1"))
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `toggleFavorite should call useCase and reload favorites`() =
        runTest {
            viewModel.favoriteVenueIds.test {
                skipItems(1)
                viewModel.toggleFavorite("v2")
                coVerify { favoriteVenuesUseCase.toggleFavorite("v2") }
                assert(awaitItem() == setOf("v1"))
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `loadSupportedCities should handle error gracefully`() =
        runTest {
            coEvery { getSupportedCities() } throws RuntimeException("fail")

            viewModel =
                VenuesViewModel(
                    getNearbyVenues,
                    getSupportedCities,
                    favoriteVenuesUseCase,
                    defaultCityName = "InvalidCity",
                )

            viewModel.locationsListState.test {
                skipItems(1)
                val error = awaitItem()
                assert(error is LocationListLoadingState.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `uiState should emit Error if venue loading fails`() =
        runTest {
            coEvery { getNearbyVenues(any()) } throws RuntimeException("Venue fetch failed")

            viewModel =
                VenuesViewModel(
                    getNearbyVenues,
                    getSupportedCities,
                    favoriteVenuesUseCase,
                    defaultCityName = testCity,
                )

            viewModel.uiState.test {
                assert(awaitItem() is VenuesUiState.Loading)
                val state = awaitItem()
                assert(state is VenuesUiState.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
