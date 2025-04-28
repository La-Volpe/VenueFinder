package de.arjmandi.venues.data.local

import de.arjmandi.venues.data.local.entity.FavoriteDao
import de.arjmandi.venues.data.local.entity.FavoriteEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteRepositoryImplTest {
	private lateinit var repository: FavoriteRepositoryImpl
	private val dao: FavoriteDao = mockk(relaxed = true)

	@Before
	fun setup() {
		repository = FavoriteRepositoryImpl(dao)
	}

	@Test
	fun `toggleFavorite should insert if not exists`() =
		runTest {
			coEvery { dao.getAllFavorites() } returns flowOf(emptyList())
			repository.toggleFavorite("venue-1")
			coVerify { dao.insertFavorite(FavoriteEntity(id = "venue-1")) }
		}

	@Test
	fun `toggleFavorite should delete if exists`() =
		runTest {
			coEvery { dao.getAllFavorites() } returns flowOf(listOf(FavoriteEntity("venue-1")))
			repository.toggleFavorite("venue-1")
			coVerify { dao.deleteFavorite("venue-1") }
		}

	@Test
	fun `isFavorite should return true when venue is favorited`() =
		runTest {
			coEvery { dao.getAllFavorites() } returns flowOf(listOf(FavoriteEntity("venue-1")))
			val isFavorite = repository.isFavorite("venue-1")
			assertTrue(isFavorite)
		}

	@Test
	fun `isFavorite should return false when venue is not favorited`() =
		runTest {
			coEvery { dao.getAllFavorites() } returns flowOf(emptyList())
			val isFavorite = repository.isFavorite("venue-1")
			assertFalse(isFavorite)
		}
}
