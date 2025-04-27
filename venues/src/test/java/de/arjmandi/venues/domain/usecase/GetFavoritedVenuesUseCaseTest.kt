package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class GetFavoritedVenuesUseCaseTest {

    private lateinit var useCase: GetFavoritedVenuesUseCase
    private val favoriteRepository: FavoriteRepository = mockk()

    @Before
    fun setup() {
        useCase = GetFavoritedVenuesUseCase(favoriteRepository)
    }

    @Test
    fun `should return favorited venue ids`() = runTest {
        // Given
        val expectedFavorites = setOf("venue-1", "venue-2", "venue-3")
        coEvery { favoriteRepository.getAllFavorite() } returns flowOf(expectedFavorites)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expectedFavorites, result)
    }
}