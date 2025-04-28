package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.repository.LocationRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObserveLocationUpdatesUseCaseTest {
	private val locationRepository: LocationRepository = mockk()
	private lateinit var useCase: ObserveLocationUpdatesUseCase

	@Before
	fun setup() {
		useCase = ObserveLocationUpdatesUseCase(locationRepository)
	}

	@Test
	fun `emits location coordinates from repository`() =
		runTest {
			val flow = flowOf(60.0 to 24.0)
			every { locationRepository.locationFlow } returns flow

			val result = useCase().first()
			assertEquals(60.0 to 24.0, result)
		}
}
