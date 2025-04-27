package de.arjmandi.venues.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun toggleFavorite(id: String)
    suspend fun isFavorite(id: String): Boolean
    suspend fun getFavoriteIds(): Flow<Set<String>>
}