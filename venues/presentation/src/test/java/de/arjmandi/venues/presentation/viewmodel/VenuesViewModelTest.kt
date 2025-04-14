package de.arjmandi.venues.presentation.viewmodel

import de.arjmandi.venues.domain.model.City.Hamburg
import de.arjmandi.venues.domain.model.City.Helsinki
import de.arjmandi.venues.domain.model.City.Stockholm
import de.arjmandi.venues.domain.model.City.Tallinn
import de.arjmandi.venues.domain.model.City.Vienna
import de.arjmandi.venues.domain.usecase.GetNearbyVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetSupportedCitiesUseCase
import de.arjmandi.venues.presentation.model.VenuesUiState
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class VenuesViewModelTest {
    lateinit var viewModel: VenuesViewModel
    lateinit var nearbyVenuesUseCase: GetNearbyVenuesUseCase
    lateinit var supportedCitiesUseCase: GetSupportedCitiesUseCase

    @Before
    fun setup() {
        supportedCitiesUseCase =
            mockk {
                every { this@mockk.invoke() } returns listOf(Helsinki, Hamburg, Vienna, Tallinn, Stockholm)
            }
        nearbyVenuesUseCase =
            mockk {
//                every { this@mockk.invoke() } returns emptyList()
            }
        viewModel =
            VenuesViewModel(
                getNearbyVenues = nearbyVenuesUseCase,
                getSupportedCitiesUseCase = supportedCitiesUseCase,
                defaultCityName = "Hamburg",
            )
    }

    @Test
    fun `test initial state`() =
        runTest {
            val initialState = viewModel.uiState.value
            assertEquals(VenuesUiState.Loading, initialState)
        }
}
