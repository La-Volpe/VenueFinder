package de.arjmandi.venues.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.arjmandi.venues.data.local.entity.FavoriteDao
import de.arjmandi.venues.data.local.entity.FavoriteEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FavoriteDaoTest {
	private lateinit var database: AppDatabase
	private lateinit var dao: FavoriteDao

	@Before
	fun setup() {
		val context = ApplicationProvider.getApplicationContext<android.content.Context>()
		database =
			Room
				.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
				.allowMainThreadQueries()
				.build()
		dao = database.favoriteDao()
	}

	@After
	fun teardown() {
		database.close()
	}

	@Test
	fun insertFavorite_adds_a_favorite_entity() =
		runTest {
			// When
			dao.insertFavorite(FavoriteEntity("venue-1"))

			// Then
			val favorites = dao.getAllFavorites().first()
			assertEquals(1, favorites.size)
			assertEquals("venue-1", favorites.first().id)
		}

	@Test
	fun deleteFavorite_removes_the_entity() =
		runTest {
			// Given
			dao.insertFavorite(FavoriteEntity("venue-1"))

			// When
			dao.deleteFavorite("venue-1")

			// Then
			val favorites = dao.getAllFavorites().first()
			assertEquals(0, favorites.size)
		}

	@Test
	fun getAllFavorites_returns_all_inserted_favorites() =
		runTest {
			// Given
			dao.insertFavorite(FavoriteEntity("venue-1"))
			dao.insertFavorite(FavoriteEntity("venue-2"))

			// When
			val favorites = dao.getAllFavorites().first()

			// Then
			assertEquals(2, favorites.size)
		}
}
