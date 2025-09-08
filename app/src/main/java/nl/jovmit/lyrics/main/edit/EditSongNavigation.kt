package nl.jovmit.lyrics.main.edit

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class EditSongDestination(
  val songId: String
) {
  constructor(savedStateHandle: SavedStateHandle) : this(
    songId = savedStateHandle["songId"] ?: ""
  )
}

fun NavGraphBuilder.editSongScreen(
  onNavigateUp: () -> Unit
) {
  composable<EditSongDestination> {
    EditSongScreen(
      onSongUpdated = onNavigateUp,
      onNavigateUp = onNavigateUp
    )
  }
}

fun NavController.navigateToSongEditor(songId: String) {
  navigate(EditSongDestination(songId = songId))
}