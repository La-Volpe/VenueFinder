package de.arjmandi.venues.data.mapper

import de.arjmandi.venues.data.remote.dto.ItemDto
import de.arjmandi.venues.domain.model.Venue

fun ItemDto.toDomain(): Venue =
	Venue(
		id = venue?.id.orEmpty(),
		name = venue?.name.orEmpty(),
		shortDescription = venue?.shortDescription.orEmpty(),
		imageUrl = image.url,
		isFavorite = false,
	)
