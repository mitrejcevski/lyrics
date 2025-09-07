package nl.jovmit.lyrics.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.theme.AppTheme

class Login : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        AppTheme {
          LoginScreen(
            onNavigateUp = { (requireActivity() as MainActivity).onSupportNavigateUp() },
            onLoggedIn = { onLoggedIn() }
          )
        }
      }
    }
  }

  private fun onLoggedIn() {
    val destination = LoginDirections.openSongsOverview()
    findNavController().navigate(destination)
  }
}
