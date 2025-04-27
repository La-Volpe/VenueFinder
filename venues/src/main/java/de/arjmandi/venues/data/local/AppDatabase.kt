package de.arjmandi.venues.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import de.arjmandi.venues.data.local.entity.FavoriteDao
import de.arjmandi.venues.data.local.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}