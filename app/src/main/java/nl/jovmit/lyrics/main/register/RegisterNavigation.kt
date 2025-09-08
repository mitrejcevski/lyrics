package nl.jovmit.lyrics.main.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object RegistrationDestination

fun NavGraphBuilder.registrationScreen(
  onRegistered: () -> Unit,
  onHaveAnAccount: () -> Unit
) {
  composable<RegistrationDestination> {
    RegisterScreen(
      onRegistered = onRegistered,
      onHaveAnAccount = onHaveAnAccount
    )
  }
}

fun NavController.navigateToRegistration() {
  navigate(RegistrationDestination)
}