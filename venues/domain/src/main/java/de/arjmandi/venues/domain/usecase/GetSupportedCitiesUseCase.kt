package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.model.City

class GetSupportedCitiesUseCase {
    operator fun invoke(): List<City> = City.all
}
