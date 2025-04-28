plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.kotlinx.serialization)
	alias(libs.plugins.kotlin.ksp)
	alias(libs.plugins.ktlint)
}

android {
	namespace = "de.arjmandi.venues"
	compileSdk = 35

	defaultConfig {
		applicationId = "de.arjmandi.venues"
		minSdk = 24
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro",
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.ktor.client.core)
	implementation(libs.ktor.client.cio)
	implementation(libs.ktor.client.content.negotiation)
	implementation(libs.ktor.serialization.kotlinx.json)
	implementation(libs.ktor.client.mock)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.room.runtime)
	implementation(libs.room.ktx)
	implementation(libs.core.ktx)
	implementation(libs.koin.core)
	implementation(libs.koin.android)
	implementation(libs.koin.compose)
	ksp(libs.room.compiler)
	implementation(libs.coil)
	testImplementation(libs.junit)
	testImplementation(libs.mockk)
	testImplementation(libs.coroutines.test)
	testImplementation(libs.room.testing)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
	testImplementation(kotlin("test"))
}
