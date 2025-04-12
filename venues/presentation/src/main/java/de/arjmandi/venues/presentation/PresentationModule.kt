package de.arjmandi.venues.presentation

import de.arjmandi.venues.presentation.viewmodel.VenuesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule =
    module {
        viewModel { VenuesViewModel(get(), get()) }
    }
