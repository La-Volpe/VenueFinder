package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.repository.FavoriteVenueRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoriteVenuesUseCaseTest {
    private lateinit var repository: FavoriteVenueRepository
    private lateinit var useCase: FavoriteVenuesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = FavoriteVenuesUseCase(repository)
    }

    @Test
    fun `getFavorites should return set of favorite venue ids from repository`() =
        runBlocking {
            // Given
            val favoriteIds = setOf("1", "2", "3")
            coEvery { repository.getFavorites() } returns favoriteIds

            // When
            val result = useCase.getFavorites()

            // Then
            assertEquals(favoriteIds, result)
            coVerify(exactly = 1) { repository.getFavorites() }
        }

    @Test
    fun `isFavorite should return true if venue id is favorite`() =
        runBlocking {
            // Given
            val venueId = "1"
            coEvery { repository.isFavorite(venueId) } returns true

            // When
            val result = useCase.isFavorite(venueId)

            // Then
            assertTrue(result)
            coVerify(exactly = 1) { repository.isFavorite(venueId) }
        }

    @Test
    fun `isFavorite should return false if venue id is not favorite`() =
        runBlocking {
            // Given
            val venueId = "2"
            coEvery { repository.isFavorite(venueId) } returns false

            // When
            val result = useCase.isFavorite(venueId)

            // Then
            assertFalse(result)
            coVerify(exactly = 1) { repository.isFavorite(venueId) }
        }

    @Test
    fun `toggleFavorite should call repository toggleFavorite`() =
        runBlocking {
            // Given
            val venueId = "3"
            coEvery { repository.toggleFavorite(venueId) } returns Unit // Mock Unit return

            // When
            useCase.toggleFavorite(venueId)

            // Then
            coVerify(exactly = 1) { repository.toggleFavorite(venueId) }
        }
}
