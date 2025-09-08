package nl.jovmit.lyrics.main.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class SongDetailsDestination(
  val songId: String
) {
  constructor(savedStateHandle: SavedStateHandle) : this(
    songId = savedStateHandle["songId"] ?: ""
  )
}

fun NavGraphBuilder.songDetailsScreen(
  onEditSong: (songId: String) -> Unit,
  onNavigateUp: () -> Unit
) {
  composable<SongDetailsDestination> {
    SongDetailsScreen(
      onSongDeleted = onNavigateUp,
      onEditSong = onEditSong,
      onNavigateUp = onNavigateUp
    )
  }
}

fun NavController.navigateToSongDetails(songId: String) {
  navigate(SongDetailsDestination(songId = songId))
}