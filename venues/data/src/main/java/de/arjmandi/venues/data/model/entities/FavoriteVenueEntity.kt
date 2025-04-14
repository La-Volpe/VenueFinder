package de.arjmandi.venues.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteVenueEntity(
    @PrimaryKey val venueId: String,
)
