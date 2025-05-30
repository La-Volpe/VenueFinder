package de.arjmandi.venues.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun FlashingFirstCharacterText(
	text: String,
	modifier: Modifier = Modifier,
	flashingDuration: Int = 100, // ms
) {
	var isVisible by remember { mutableStateOf(true) }

	// Timer to toggle visibility every 100ms
	LaunchedEffect(Unit) {
		while (true) {
			delay(flashingDuration.toLong())
			isVisible = !isVisible
		}
	}

	// Split the first grapheme cluster (emoji-aware)
	val (firstChar, remainingText) =
		remember(text) {
			if (text.isEmpty()) {
				Pair("", "")
			} else {
				val firstGrapheme =
					text
						.takeWhile {
							// Ensure we capture the full emoji (including modifiers)
							it.isEmojiPart()
						}.ifEmpty { text.take(1) }
				Pair(firstGrapheme, text.substring(firstGrapheme.length))
			}
		}

	Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
		AnimatedVisibility(
			visible = isVisible,
			enter = fadeIn(),
			exit = fadeOut(),
		) {
			Text(
				text = firstChar,
			)
		}
		Text(remainingText)
	}
}

// Helper to check if a character is part of an emoji sequence
private fun Char.isEmojiPart(): Boolean =
	when (this.category) {
		CharCategory.SURROGATE,
		CharCategory.OTHER_SYMBOL,
		CharCategory.MODIFIER_SYMBOL,
		-> true
		else -> false
	}
