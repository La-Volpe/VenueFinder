package de.arjmandi.venues.presentation.viewmodel

import app.cash.turbine.test
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.usecase.GetNearbyVenuesUseCase
import de.arjmandi.venues.presentation.model.VenuesUiState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VenuesViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val mockUseCase = mockk<GetNearbyVenuesUseCase>()
    private lateinit var viewModel: VenuesViewModel

    private val venues =
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

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = VenuesViewModel(mockUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadVenues emits Loading then Success`() =
        runTest {
            coEvery { mockUseCase(any()) } returns venues

            viewModel.uiState.test {
                viewModel.loadVenues(Location(60.169, 24.931))
                dispatcher.scheduler.advanceUntilIdle()

                assertTrue(awaitItem() is VenuesUiState.Loading)
                val success = awaitItem()
                assertTrue(success is VenuesUiState.Success)
                assertEquals(6, (success as VenuesUiState.Success).venues.size)
            }
        }

    @Test
    fun `loadVenues emits Error when use case throws`() =
        runTest {
            coEvery { mockUseCase(any()) } throws RuntimeException("Network error")

            viewModel.uiState.test {
                viewModel.loadVenues(Location(60.169, 24.931))
                dispatcher.scheduler.advanceUntilIdle()

                assertTrue(awaitItem() is VenuesUiState.Loading)
                val error = awaitItem()
                assertTrue(error is VenuesUiState.Error)
                assertEquals("Failed to load venues", (error as VenuesUiState.Error).message)
            }
        }

    @Test
    fun `loadVenues handles cancellation gracefully`() =
        runTest {
            coEvery { mockUseCase(any()) } coAnswers {
                delay(1000)
                venues
            }

            viewModel.uiState.test {
                val job =
                    launch {
                        viewModel.loadVenues(Location(60.169, 24.931))
                    }
                job.cancel()
                assertTrue(awaitItem() is VenuesUiState.Loading)
            }
        }
}
