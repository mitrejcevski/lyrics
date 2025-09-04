package nl.jovmit.lyrics.main.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.jovmit.lyrics.R
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.theme.AppTheme

class SongsOverview : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        AppTheme {
          SongsOverviewScreen(
            onAddNewSong = { openNewSong() },
            onLoggedOut = { openRegistration() },
            onOpenSongDetails = { openSongDetails(it) }
          )
        }
      }
    }
  }

  private fun openRegistration() {
    val destination = SongsOverviewDirections.openRegistration()
    findNavController().navigate(destination)
  }

  private fun openNewSong() {
    findNavController().navigate(R.id.openNewSong)
  }

  private fun openSongDetails(song: Song) {
    val direction = SongsOverviewDirections.openSongDetails(song.songId.value)
    findNavController().navigate(direction)
  }
}
