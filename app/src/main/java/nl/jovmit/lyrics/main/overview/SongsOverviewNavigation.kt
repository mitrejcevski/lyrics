package nl.jovmit.lyrics.main.overview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
private data object SongsOverviewDestination

fun NavGraphBuilder.songsOverviewScreen(
  onAddNewSong: () -> Unit,
  onLoggedOut: () -> Unit,
  onOpenSongDetails: (songId: String) -> Unit
) {
  composable<SongsOverviewDestination> {
    SongsOverviewScreen(
      onAddNewSong = onAddNewSong,
      onLoggedOut = onLoggedOut,
      onOpenSongDetails = { onOpenSongDetails(it.songId.value) }
    )
  }
}

fun NavController.navigateToSongsOverview() {
  navigate(SongsOverviewDestination)
}