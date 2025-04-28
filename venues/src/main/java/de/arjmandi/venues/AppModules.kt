package de.arjmandi.venues

import androidx.room.Room
import de.arjmandi.venues.data.constants.LocationRepositoryImpl
import de.arjmandi.venues.data.local.AppDatabase
import de.arjmandi.venues.data.local.FavoriteRepositoryImpl
import de.arjmandi.venues.data.remote.VenueRemoteDataSource
import de.arjmandi.venues.data.remote.VenueRepositoryImpl
import de.arjmandi.venues.data.remote.WoltApiService
import de.arjmandi.venues.domain.repository.FavoriteRepository
import de.arjmandi.venues.domain.repository.LocationRepository
import de.arjmandi.venues.domain.repository.VenueRepository
import de.arjmandi.venues.domain.usecase.GetFavoritedVenuesUseCase
import de.arjmandi.venues.domain.usecase.GetVenuesForLocationUseCase
import de.arjmandi.venues.domain.usecase.ObserveLocationUpdatesUseCase
import de.arjmandi.venues.domain.usecase.ToggleFavoriteUseCase
import de.arjmandi.venues.presentation.view.VenuesListContext
import de.arjmandi.venues.presentation.view.VenuesListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val domainModule =
	module {
		factory { GetFavoritedVenuesUseCase(get<FavoriteRepository>()) }
		factory { GetVenuesForLocationUseCase(get<VenueRepository>()) }
		factory { ObserveLocationUpdatesUseCase(get<LocationRepository>()) }
		factory { ToggleFavoriteUseCase(get<FavoriteRepository>()) }
	}

val dataModule =
	module {
		single {
			Room
				.databaseBuilder(
					androidContext(),
					AppDatabase::class.java,
					"venues-db",
				).build()
		}
		single { get<AppDatabase>().favoriteDao() }

		// Repository implementations
		single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
		single<VenueRepository> { VenueRepositoryImpl(get()) }
		single<LocationRepository> { LocationRepositoryImpl() }

		single {
			HttpClient(CIO) {
				install(ContentNegotiation) {
					json(
						Json {
							ignoreUnknownKeys = true
						},
					)
				}
				install(Logging) {
					logger = Logger.DEFAULT
					level = LogLevel.HEADERS
				}
			}
		}

		// API Service
		single { WoltApiService(get()) }
		single { VenueRemoteDataSource(get()) }
	}

val presentationModule =
	module {
		viewModel {
			VenuesListViewModel(
				VenuesListContext(get(), get(), get(), get()),
			)
		}
	}

val appModules =
	listOf(
		domainModule,
		dataModule,
		presentationModule,
	)
