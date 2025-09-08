package nl.jovmit.lyrics.main.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.theme.AppTheme

class Register : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        AppTheme {
          RegisterScreen(
            onRegistered = { onRegistrationSuccess() },
            onHaveAnAccount = { openLogin() }
          )
        }
      }
    }
  }

  private fun openLogin() {
    val destination = RegisterDirections.openLogin()
    findNavController().navigate(destination)
  }

  private fun onRegistrationSuccess() {
    val destination = RegisterDirections.openSongsOverview()
    findNavController().navigate(destination)
  }
}
