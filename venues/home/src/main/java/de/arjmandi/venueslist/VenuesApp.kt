package de.arjmandi.venueslist

import android.app.Application
import de.arjmandi.venueslist.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VenuesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@VenuesApp)
            modules(homeModule)
        }
    }
}
