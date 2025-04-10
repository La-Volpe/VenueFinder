package de.arjmandi.venues.data.mapper

import de.arjmandi.venues.data.model.dto.ImageDto
import de.arjmandi.venues.data.model.dto.VenueDto
import org.junit.Assert.assertEquals
import org.junit.Test

class VenueMapperTest {
    @Test
    fun `should map VenueDto to Venue domain model`() {
        val dto =
            VenueDto(
                id = "123",
                name = "Test Venue",
                shortDescription = "Great food here",
                brandImage = ImageDto(url = "http://example.com/image.jpg"),
            )

        val domain = dto.toDomain()

        assertEquals("123", domain.id)
        assertEquals("Test Venue", domain.name)
        assertEquals("Great food here", domain.shortDescription)
        assertEquals("http://example.com/image.jpg", domain.imageUrl)
    }

    @Test
    fun `should return empty strings for null fields`() {
        val dto =
            VenueDto(
                id = "456",
                name = "Fallback Venue",
                shortDescription = null,
                brandImage = null,
            )

        val domain = dto.toDomain()

        assertEquals("456", domain.id)
        assertEquals("Fallback Venue", domain.name)
        assertEquals("", domain.shortDescription)
        assertEquals("", domain.imageUrl)
    }
}
