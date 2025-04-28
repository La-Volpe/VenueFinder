package de.arjmandi.venues

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VenuesApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@VenuesApplication)
			modules(appModules)
		}
	}
}
