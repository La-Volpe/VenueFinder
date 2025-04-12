package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.model.Location
import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.repository.VenueRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetNearbyVenuesUseCaseTest {
    private lateinit var repository: VenueRepository
    private lateinit var useCase: GetNearbyVenuesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetNearbyVenuesUseCase(repository)
    }

    @Test
    fun `should return up to 15 venues for a given location`() =
        runBlocking {
            val venues =
                (1..20).map {
                    Venue(
                        id = it.toString(),
                        name = "Venue $it",
                        shortDescription = "Description $it",
                        imageUrl = "http://example.com/image$it.jpg",
                    )
                }

            coEvery { repository.getNearbyVenues(any()) } returns venues

            val result = useCase.invoke(Location(60.169418, 24.931618))

            assertEquals(15, result.size)
        }
}
