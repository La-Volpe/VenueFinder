package de.arjmandi.venues.data.remote

import de.arjmandi.venues.domain.model.Venue
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test

class VenueRemoteDataSourceTestWithJson {
	private lateinit var mockEngine: MockEngine
	private lateinit var client: HttpClient
	private lateinit var apiService: WoltApiService
	private lateinit var dataSource: VenueRemoteDataSource

	@Test
	fun toDomain_whenDtoIsValid_shouldReturnDomainModel() =
		runBlocking {
			mockEngine =
				MockEngine {
					respond(
						content = loadJsonFromResource(),
						status = HttpStatusCode.OK,
						headers = headersOf(HttpHeaders.ContentType, "application/json"),
					)
				}

			client =
				HttpClient(mockEngine) {
					install(ContentNegotiation) {
						json(Json { ignoreUnknownKeys = true })
					}
				}
			apiService = WoltApiService(client)
			apiService = WoltApiService(client)
			dataSource = VenueRemoteDataSource(apiService)
			val expectedVenues = venues
			lateinit var dto: List<Venue>
			dataSource.getVenuesWithApiResult(60.1695, 24.9354).collectLatest {
				when (it) {
					is ApiResult.Success -> {
						dto = it.data
						dto.take(3).forEachIndexed { index, venue ->
							assert(venue.id == expectedVenues[index].id)
							assert(venue.name == expectedVenues[index].name)
							assert(venue.shortDescription == expectedVenues[index].shortDescription)
							assert(venue.imageUrl == expectedVenues[index].imageUrl)
							assert(venue.isFavorite == expectedVenues[index].isFavorite)
						}
					}

					is ApiResult.HttpError -> {
						assert(false) { "HTTP error: ${it.code} - ${it.message}" }
					}

					is ApiResult.NetworkError -> {
						assert(false) { "Network error: ${it.exception.message}" }
					}
				}
			}
		}

	private fun loadJsonFromResource(): String {
		val path = "/full_real_response.json"
		return javaClass.getResource(path)?.readText()
			?: throw IllegalArgumentException("Resource not found: $path")
	}

	private val venues =
		listOf(
			Venue(
				id = "5ae6013cf78b5a000bb64022",
				name = "McDonald's Helsinki Kamppi",
				shortDescription = "I'm lovin' it.",
				imageUrl = "https://imageproxy.wolt.com/assets/6735be5986f45b72713e2129",
				isFavorite = false,
			),
			Venue(
				id = "630723a28b2af6d016acbd64",
				name = "kot.TENNISPALATSI",
				shortDescription = "We Kot you.",
				imageUrl = "https://imageproxy.wolt.com/assets/67321481e34d3d02aa5a2ef4",
				isFavorite = false,
			),
			Venue(
				id = "5cc175b2daaaee24fdbb92ee",
				name = "Taco Bell Tennispalatsi",
				shortDescription = "Meksikolainen pikaruokaravintola",
				imageUrl = "https://imageproxy.wolt.com/assets/67332fd1c59f3326de5434ed",
				isFavorite = false,
			),
		)
}
