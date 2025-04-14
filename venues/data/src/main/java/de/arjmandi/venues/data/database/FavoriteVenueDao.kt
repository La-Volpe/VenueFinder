package de.arjmandi.venues.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.arjmandi.venues.data.model.entities.FavoriteVenueEntity

@Dao
interface FavoriteVenueDao {
    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<FavoriteVenueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteVenueEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteVenueEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE venueId = :venueId)")
    suspend fun isFavorite(venueId: String): Boolean
}
