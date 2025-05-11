package de.arjmandi.venues.util

import de.arjmandi.venues.data.remote.ApiResult

object ErrorMessageProvider {
	fun getErrorMessage(error: ApiResult.HttpError): String =
		when (error.code) {
			429 -> "You’re making too many requests. Please wait a moment and try again."
			404 -> "We couldn’t find what you’re looking for."
			500 -> "Server is having issues. Try again later."
			501 -> "This feature isn’t available right now."
			502 -> "Bad gateway. Something went wrong."
			503 -> "Service is temporarily unavailable. Check back soon!"
			504 -> "Server timeout. Please try again."
			else -> "An unexpected error occurred. (Error code: ${error.code})"
		}
}
