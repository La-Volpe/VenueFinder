package de.arjmandi.venues.presentation.di

import de.arjmandi.venues.presentation.viewmodel.VenuesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule =
    module {
        viewModel { VenuesViewModel(get()) }
    }
