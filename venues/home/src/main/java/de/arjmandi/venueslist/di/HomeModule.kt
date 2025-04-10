package de.arjmandi.venueslist.di

import de.arjmandi.venues.data.dataModule
import de.arjmandi.venues.domain.domainModule
import de.arjmandi.venues.presentation.presentationModule

val homeModule = listOf(presentationModule, dataModule, domainModule)
