package de.arjmandi.venues.data.remote

import de.arjmandi.venues.domain.model.Venue
import de.arjmandi.venues.domain.repository.VenueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VenueRepositoryImpl(
	private val dataSource: VenueRemoteDataSource,
) : VenueRepository {
	override fun getVenues(
		lat: Double,
		lon: Double,
	): Flow<List<Venue>> =
		dataSource
			.getVenuesWithApiResult(lat, lon)
			.map { result ->
				when (result) {
					is ApiResult.Success -> result.data.take(15) // Take first 15 venues
					is ApiResult.HttpError -> throw Exception("HTTP Error: ${result.code} - ${result.message}")
					is ApiResult.NetworkError -> throw result.exception
				}
			}
}
