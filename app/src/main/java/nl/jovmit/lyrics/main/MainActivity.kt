package nl.jovmit.lyrics.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import nl.jovmit.lyrics.main.preferences.UserPreferencesViewModel
import nl.jovmit.lyrics.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val userPreferencesViewModel by viewModel<UserPreferencesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }

    private fun performLoggedInUserCheck() {
        userPreferencesViewModel.getLoggedInUser()?.let {
            //TODO Perform LoggedIn Check On Startup
        }
    }
}