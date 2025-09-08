package nl.jovmit.lyrics.main.add

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
private data object NewSongDestination

fun NavGraphBuilder.addNewSongScreen(
  onNavigateUp: () -> Unit,
) {
  composable<NewSongDestination> {
    NewSongScreen(
      onNavigateUp = onNavigateUp,
      onSongSaved = onNavigateUp
    )
  }
}

fun NavController.navigateToNewSong() {
  navigate(NewSongDestination)
}