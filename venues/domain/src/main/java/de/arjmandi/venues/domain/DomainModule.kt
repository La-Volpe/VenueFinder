package de.arjmandi.venues.domain

import de.arjmandi.venues.domain.repository.VenueRepository
import de.arjmandi.venues.domain.usecase.GetNearbyVenuesUseCase
import org.koin.dsl.module

val domainModule =
    module {
        factory { GetNearbyVenuesUseCase(get<VenueRepository>()) }
    }
