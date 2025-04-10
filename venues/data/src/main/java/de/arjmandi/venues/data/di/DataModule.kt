package de.arjmandi.venues.data.di

import de.arjmandi.venues.data.api.WoltApiService
import de.arjmandi.venues.data.repository.VenueRepositoryImpl
import de.arjmandi.venues.domain.repository.VenueRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
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
