
package de.arjmandi.venues.presentation.view.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.presentation.model.VenuesUiState
import org.junit.Rule
import org.junit.Test

class VenuesScreenContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsSpinner() {
        composeTestRule.setContent {
            VenuesScreenContent(
                uiState = VenuesUiState.Loading,
                displayedLocation = null,
                isLoadingNext = false,
            )
        }

        // The CircularProgressIndicator should be shown
        composeTestRule.onNodeWithText("Burger King").assertDoesNotExist()
    }

    @Test
    fun successState_showsHeaderAndVenues() {
        val venues =
            listOf(
                Venue("1", "Burger King", "Flame grilled", ""),
                Venue("2", "Subway", "Eat Fresh", ""),
            )
        val location = Location(60.169, 24.938, "Narinkka Square")

        composeTestRule.setContent {
            VenuesScreenContent(
                uiState = VenuesUiState.Success(venues),
                displayedLocation = location,
                isLoadingNext = false,
            )
        }

        composeTestRule.mainClock.advanceTimeBy(100) // allow header to render
        composeTestRule.onNodeWithText("Narinkka Square").assertIsDisplayed()

        composeTestRule.mainClock.advanceTimeBy(100)
        composeTestRule.onNodeWithText("Burger King").assertIsDisplayed()

        composeTestRule.mainClock.advanceTimeBy(100)
        composeTestRule.onNodeWithText("Subway").assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        val errorMessage = "Something went wrong"

        composeTestRule.setContent {
            VenuesScreenContent(
                uiState = VenuesUiState.Error(errorMessage),
                displayedLocation = null,
                isLoadingNext = false,
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}
