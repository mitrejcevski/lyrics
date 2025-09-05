package nl.jovmit.lyrics.main.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.overview.SongsRepository

private fun SavedStateHandle.toSongDetailsNavArgs(): SongDetailsArgs {
  return SongDetailsArgs(get("songId") ?: "")
}

class SongDetailsViewModel(
  private val songsRepository: SongsRepository,
  private val dispatchers: CoroutineDispatchers,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val args = savedStateHandle.toSongDetailsNavArgs()

  private val _uiState = MutableStateFlow(SongDetailsScreenState())
  val uiState: StateFlow<SongDetailsScreenState> = _uiState

  fun dismissSongNotFoundError() {
    _uiState.update { it.copy(isSongNotFoundError = false) }
  }

  fun dismissSongDeletingError() {
    _uiState.update { it.copy(isSongDeletingError = false) }
  }

  fun loadSong() {
    _uiState.update { it.copy(isLoading = true) }
    viewModelScope.launch {
      val result = withContext(dispatchers.background) {
        songsRepository.findSongById(args.songId)
      }
      onSongResult(result)
    }
  }

  fun deleteSong() {
    _uiState.update { it.copy(isLoading = true) }
    viewModelScope.launch {
      val result = withContext(dispatchers.background) {
        songsRepository.deleteSongById(args.songId)
      }
      onSongResult(result)
    }
  }

  private fun onSongResult(result: SongResult) {
    when (result) {
      is SongResult.Fetched -> _uiState.update { it.copy(isLoading = false, song = result.song) }
      is SongResult.NotFound -> _uiState.update {
        it.copy(
          isLoading = false,
          isSongNotFoundError = true
        )
      }

      is SongResult.Deleted -> _uiState.update { it.copy(isLoading = false, isSongDeleted = true) }
      is SongResult.ErrorDeleting -> _uiState.update {
        it.copy(
          isLoading = false,
          isSongDeletingError = true
        )
      }

      else -> {}
    }
  }
}
