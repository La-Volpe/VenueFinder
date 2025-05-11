package de.arjmandi.venues.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.arjmandi.venues.presentation.view.VenuesListScreen
import de.arjmandi.venues.presentation.view.VenuesListViewModel
import de.arjmandi.venues.ui.theme.Venuesv2Theme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
	private val viewModel: VenuesListViewModel by viewModel()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			Venuesv2Theme {
				VenuesListScreen(viewModel)
			}
		}
	}
}

@Composable
fun Greeting(
	name: String,
	modifier: Modifier = Modifier,
) {
	Text(
		text = "Hello $name!",
		modifier = modifier,
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	Venuesv2Theme {
		Greeting("Android")
	}
}
