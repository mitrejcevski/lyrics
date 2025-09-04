package nl.jovmit.lyrics.main.details

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

class SongDetails : Fragment() {

  private val infoViewModel by activityViewModel<InfoViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        AppTheme {
          SongDetailsScreen(
            onNavigateUp = { (requireActivity() as MainActivity).onSupportNavigateUp() },
            onEditSong = { editSong(it) },
            onSongDeleted = { onSongDeleted() }
          )
        }
      }
    }
  }

  private fun editSong(songId: String) {
    val direction = SongDetailsDirections.editSong(songId)
    findNavController().navigate(direction)
  }

  private fun onSongDeleted() {
    infoViewModel.showInfo(getString(R.string.success))
    findNavController().navigateUp()
  }
}