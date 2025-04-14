package de.arjmandi.venues.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.arjmandi.venues.data.model.entities.FavoriteVenueEntity

@Database(
    entities = [FavoriteVenueEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVenueDao(): FavoriteVenueDao
}
