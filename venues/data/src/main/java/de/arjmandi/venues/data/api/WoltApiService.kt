package de.arjmandi.venues.data.api

import de.arjmandi.venues.data.model.dto.VenueResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType

class WoltApiService(
    private val client: HttpClient,
) {
    suspend fun getVenues(
        latitude: Double,
        longitude: Double,
    ): VenueResponseDto =
        client
            .get("https://restaurant-api.wolt.com/v1/pages/restaurants") {
                parameter("lat", latitude)
                parameter("lon", longitude)
                accept(ContentType.Application.Json)
            }.body()
}
