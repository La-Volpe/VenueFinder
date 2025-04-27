package de.arjmandi.venues.data.remote

import de.arjmandi.venues.data.mapper.toDomain
import de.arjmandi.venues.domain.model.Venue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class VenueRemoteDataSource(
    private val apiService: WoltApiService
) {
    fun getVenues(lat: Double, lon: Double): Flow<ApiResult<List<Venue>>> = flow {
        when (val result = apiService.getVenues(lat, lon)) {
            is ApiResult.Success -> {
                val venues = result.data.sections
                    .firstOrNull { it.name == "restaurants-delivering-venues" }
                    ?.items?.map{ it.toDomain() }

                if(venues.isNullOrEmpty()) {
                    emit(ApiResult.HttpError(404, "No venues found near this location"))
                    return@flow
                }
                emit(ApiResult.Success(venues))
            }

            is ApiResult.HttpError -> emit(ApiResult.HttpError(result.code, result.message))
            is ApiResult.NetworkError -> emit(ApiResult.NetworkError(result.exception))
        }
    }
}