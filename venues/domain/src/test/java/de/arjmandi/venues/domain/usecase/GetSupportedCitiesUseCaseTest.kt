package de.arjmandi.venues.domain.usecase

import de.arjmandi.venues.domain.model.City
import de.arjmandi.venues.domain.model.Location
import kotlin.test.Test
import kotlin.test.assertTrue

class GetSupportedCitiesUseCaseTest {
    private val useCase = GetSupportedCitiesUseCase()

    @Test
    fun `every city should have at least 3 coordinate`() {
        val cities = useCase()
        cities.forEach {
            assertTrue(it.coordinates.size >= 3, "${it.displayName} has no coordinates")
        }
    }

    @Test
    fun `every city should have at least 3 coordinates`() {
        val cities = useCase()
        cities.forEach {
            assertTrue(it.coordinates.size >= 3, "${it.displayName} has less than 3 coordinates")
        }
    }

    @Test
    fun `all coordinates are within city boundaries`() {
        City.all.forEach { city ->
            city.coordinates.forEach { location ->
                assertTrue(
                    isCoordinateWithinCityBoundary(city, location),
                    "Location $location for ${city.displayName} is outside city boundaries",
                )
            }
        }
    }

    @Suppress("ktlint:standard:max-line-length")
    private fun isCoordinateWithinCityBoundary(
        city: City,
        location: Location,
    ): Boolean =
        location.latitude in city.cityBounds.latMin..city.cityBounds.latMax &&
            location.longitude in city.cityBounds.lonMin..city.cityBounds.lonMax
}
