package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.repository.FavoriteRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class ToggleFavoriteUseCaseTest {

 private val favoriteRepository: FavoriteRepository = mockk(relaxed = true)
 private lateinit var useCase: ToggleFavoriteUseCase

 @Before
 fun setup() {
  useCase = ToggleFavoriteUseCase(favoriteRepository)
 }

 @Test
 fun `toggles favorite state`() = runTest {
  useCase("venue123")
  coVerify { favoriteRepository.toggleFavorite("venue123") }
 }
}