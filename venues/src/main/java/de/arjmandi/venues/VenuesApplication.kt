package de.arjmandi.venues

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

class VenuesApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@VenuesApplication)
			modules(appModules)
		}
	}
	override fun onTerminate() {
		getKoin().get<CoroutineScope>().cancel()
		stopKoin()
		super.onTerminate()
	}
}
