package de.arjmandi.venues.data.mapper

import de.arjmandi.venues.data.model.dto.VenueDto
import de.arjmandi.venues.domain.model.Venue

fun VenueDto.toDomain(): Venue =
    Venue(
        id = id,
        name = name,
        shortDescription = shortDescription.orEmpty(),
        imageUrl = brandImage?.url.orEmpty(),
    )
