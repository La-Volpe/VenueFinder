package de.arjmandi.venues.data.remote

import de.arjmandi.venues.data.remote.dto.VenueResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlin.coroutines.cancellation.CancellationException

class WoltApiService(
	private val client: HttpClient,
) {
	suspend fun getVenues(
		lat: Double,
		lon: Double,
	): ApiResult<VenueResponseDto> =
		try {
			val response: HttpResponse =
				client.get("https://restaurant-api.wolt.com/v1/pages/restaurants") {
					url {
						parameters.append("lat", lat.toString())
						parameters.append("lon", lon.toString())
					}
				}
			when (response.status) {
				HttpStatusCode.OK -> {
					val body = response.body<VenueResponseDto>()
					ApiResult.Success(body)
				}
				HttpStatusCode.TooManyRequests -> ApiResult.HttpError(429, "Rate limit exceeded. Please slow down.")
				HttpStatusCode.NotFound -> ApiResult.HttpError(404, "Requested resource not found.")
				HttpStatusCode.InternalServerError -> ApiResult.HttpError(500, "Internal server error.")
				HttpStatusCode.NotImplemented -> ApiResult.HttpError(501, "Server does not support this feature.")
				HttpStatusCode.BadGateway -> ApiResult.HttpError(502, "Bad gateway error.")
				HttpStatusCode.ServiceUnavailable -> ApiResult.HttpError(503, "Service is temporarily unavailable.")
				HttpStatusCode.GatewayTimeout -> ApiResult.HttpError(504, "Gateway timeout error.")
				else -> ApiResult.HttpError(response.status.value, "Unexpected error: ${response.status.description}")
			}
		} catch (e: CancellationException) {
			throw e
		} catch (e: Throwable) {
			ApiResult.NetworkError(e)
		}
}
