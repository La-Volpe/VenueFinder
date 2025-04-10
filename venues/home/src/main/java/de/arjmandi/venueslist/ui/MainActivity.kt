package de.arjmandi.venueslist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.arjmandi.venues.presentation.view.VenuesScreen
import de.arjmandi.venues.presentation.viewmodel.VenuesViewModel
import de.arjmandi.venueslist.ui.theme.VenuesTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: VenuesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadVenues()
        setContent {
            VenuesTheme {
                VenuesScreen(viewModel = viewModel)
            }
        }
    }
}
