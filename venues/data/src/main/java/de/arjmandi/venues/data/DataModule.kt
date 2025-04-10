package de.arjmandi.venues.data

import de.arjmandi.venues.data.api.WoltApiService
import de.arjmandi.venues.data.repository.VenueRepositoryImpl
import de.arjmandi.venues.domain.repository.VenueRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule =
    module {
        single {
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                        },
                    )
                }
            }
        }

        single { WoltApiService(get()) }

        single<VenueRepository> {
            VenueRepositoryImpl(get())
        }
    }
