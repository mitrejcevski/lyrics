package nl.jovmit.lyrics.main.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
private data object LoginDestination

fun NavGraphBuilder.loginScreen(
  onNavigateUp: () -> Unit,
  onLoggedIn: () -> Unit
) {
  composable<LoginDestination> {
    LoginScreen(
      onLoggedIn = onLoggedIn,
      onNavigateUp = onNavigateUp
    )
  }
}

fun NavController.navigateToLogin() {
  navigate(LoginDestination)
}