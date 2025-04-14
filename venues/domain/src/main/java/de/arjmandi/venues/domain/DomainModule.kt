package de.arjmandi.venues.domain

import de.arjmandi.venues.domain.repository.FavoriteVenueRepository
import de.arjmandi.venues.domain.repository.VenueRepository
import de.arjmandi.venues.domain.usecase.FavoriteVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetNearbyVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetSupportedCitiesUseCase
import org.koin.dsl.module

val domainModule =
    module {
        factory { GetNearbyVenuesUseCase(get<VenueRepository>()) }
        factory { GetSupportedCitiesUseCase() }
        factory { FavoriteVenuesUseCase(get<FavoriteVenueRepository>()) }
    }
