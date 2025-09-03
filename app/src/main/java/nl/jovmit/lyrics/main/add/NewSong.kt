package nl.jovmit.lyrics.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.main.InfoViewModel
import nl.jovmit.lyrics.main.MainActivity
import nl.jovmit.lyrics.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class NewSong : Fragment() {

  private val infoViewModel by activityViewModel<InfoViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        AppTheme {
          NewSongScreen(
            onNavigateUp = { (requireActivity() as MainActivity).onSupportNavigateUp() },
            onSongSaved = { displaySongAddingSuccess() }
          )
        }
      }
    }
  }

  private fun displaySongAddingSuccess() {
    infoViewModel.showInfo(getString(R.string.success))
    findNavController().navigateUp()
  }
}