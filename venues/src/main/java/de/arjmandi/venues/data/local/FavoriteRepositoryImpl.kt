package de.arjmandi.venues.data.local

import de.arjmandi.venues.data.local.entity.FavoriteDao
import de.arjmandi.venues.data.local.entity.FavoriteEntity
import de.arjmandi.venues.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
	private val dao: FavoriteDao,
) : FavoriteRepository {
	override suspend fun toggleFavorite(id: String) {
		val currentFavorites = dao.getAllFavorites().first().map { it.id }
		if (currentFavorites.contains(id)) {
			dao.deleteFavorite(id)
		} else {
			dao.insertFavorite(FavoriteEntity(id))
		}
	}

	override suspend fun isFavorite(id: String): Boolean = !getAllFavorite().firstOrNull { it.contains(id) }.isNullOrEmpty()

	override suspend fun getAllFavorite(): Flow<Set<String>> = dao.getAllFavorites().map { it.map { entity -> entity.id }.toSet() }
}
