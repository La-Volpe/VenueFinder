package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.repository.FavoriteRepository
import de.arjmandi.venues.domain.repository.VenueRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetVenuesForLocationUseCaseTest {
	private val venueRepository: VenueRepository = mockk()
	private val favoriteRepository: FavoriteRepository = mockk()
	private lateinit var useCase: GetVenuesForLocationUseCase

	@Before
	fun setup() {
		useCase = GetVenuesForLocationUseCase(venueRepository)
	}

	@Test
	fun `returns top 15 venues`() =
		runTest {
			val venues =
				(1..20).map {
					Venue(it.toString(), "Venue $it", "Description", "image", false)
				}

			coEvery { venueRepository.getVenues(any(), any()) } returns flowOf(venues)

			val result = useCase(0.0, 0.0).first()

			assertEquals(15, result.size)
		}
}
