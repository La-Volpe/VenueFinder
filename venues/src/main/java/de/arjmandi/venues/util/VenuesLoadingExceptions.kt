package de.arjmandi.venues.util

data class BackendLoadingException(
	override val message: String,
) : Exception(message)

data class NetworkLoadingException(
	override val message: String,
) : Exception(message)
