package de.arjmandi.venues.data.remote

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

class VenueRemoteDataSourceTest {
    private lateinit var mockEngine: MockEngine
    private lateinit var client: HttpClient
    private lateinit var apiService: WoltApiService
    private lateinit var dataSource: VenueRemoteDataSource

    private fun setupMockEngine(response: String, statusCode: HttpStatusCode, isJsonContent: Boolean = false) {
        mockEngine = MockEngine {
            respond(
                content = response,
                status = statusCode,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        apiService = WoltApiService(client)
        dataSource = VenueRemoteDataSource(apiService)

    }

    @Test
    fun getVenues_WithApiResult_receives_rateLimit() = runBlocking {
        setupMockEngine(
            response = "",
            statusCode = HttpStatusCode.TooManyRequests
        )
        dataSource.getVenuesWithApiResult(60.1695, 24.9354).collectLatest {
            when (it) {
                is ApiResult.Success -> assert(false) { "Expected an error, but got success." }
                is ApiResult.HttpError -> assert(it.code == 429) { "Expected rate limit error, but got ${it.code}." }
                is ApiResult.NetworkError -> assert(false) { "Expected a success, but got network error." }
            }
        }
    }

    @Test
    fun getVenues_WithApiResult_receives_notFound() = runBlocking {
        setupMockEngine(
            response = "",
            statusCode = HttpStatusCode.NotFound
        )
        dataSource.getVenuesWithApiResult(60.1695, 24.9354).collectLatest {
            when (it) {
                is ApiResult.Success -> assert(false) { "Expected an error, but got success." }
                is ApiResult.HttpError -> assert(it.code == 404) { "Expected not found error, but got ${it.code}." }
                is ApiResult.NetworkError -> assert(false) { "Expected a success, but got network error." }
            }
        }
    }

    @Test
    fun getVenues_receives_responseButThereIsNoVenuesWithApiResult() = runBlocking {
        setupMockEngine(
            response = noContentJson,
            statusCode = HttpStatusCode.OK,
            isJsonContent = true
        )
        dataSource.getVenuesWithApiResult(60.1695, 24.9354).collectLatest {
            when (it) {
                is ApiResult.Success -> assert(false) { "Expected an error, but got success." }
                is ApiResult.HttpError -> assert(it.code == 404) { "Expected not found error, but got ${it.code}." }
                is ApiResult.NetworkError -> assert(false) { "Expected a success, but got network error. ${it.exception.message}" }
            }
        }
    }

    val noContentJson = """
        {
    "created": {
        "$/date": 1745777425702
    },
    "expires_in_seconds": 900,
    "name": "restaurants",
    "page_title": "Restaurants",
    "sections": [
        {
            "description": "It\u2019s not you, it\u2019s us! We\u2019re working hard to expand and hope to come to your area soon \ud83d\ude0c",
            "link": {
                "target": "",
                "target_sort": "delivers-to",
                "target_title": "",
                "title": "",
                "type": "no-link"
            },
            "name": "no-content",
            "template": "no-content",
            "title": "There aren\u2019t any restaurants on Wolt near you yet \ud83d\ude15"
        }
    ],
    "show_large_title": false,
    "show_map": false,
    "track_id": "discovery:restaurants"
}
    """.trimIndent()
}