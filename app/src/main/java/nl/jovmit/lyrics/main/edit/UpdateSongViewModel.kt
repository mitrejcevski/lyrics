package nl.jovmit.lyrics.main.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.overview.SongsRepository

private fun SavedStateHandle.toEditSongArgs(): EditSongArgs {
  return EditSongArgs(songId = get("songId") ?: "")
}

class UpdateSongViewModel(
  private val songsRepository: SongsRepository,
  private val dispatchers: CoroutineDispatchers,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val args = savedStateHandle.toEditSongArgs()

  private val _uiState = MutableStateFlow(EditSongScreenState())
  val uiState: StateFlow<EditSongScreenState> = _uiState

  fun setSongData(song: Song) {
    _uiState.update {
      it.copy(
        songTitle = song.songTitle.value,
        songPerformer = song.songPerformer.name,
        songLyrics = song.songLyric.lyrics
      )
    }
  }

  fun updateSongTitle(newTitle: String) {
    _uiState.update { it.copy(songTitle = newTitle, isEmptySongTitleError = false) }
  }

  fun updateSongPerformer(newPerformer: String) {
    _uiState.update { it.copy(songPerformer = newPerformer, isEmptyPerformerNameError = false) }
  }

  fun updateSongLyrics(newLyrics: String) {
    _uiState.update { it.copy(songLyrics = newLyrics, isEmptyLyricsError = false) }
  }

  fun dismissErrorBanner() {
    _uiState.update { it.copy(isErrorSavingSong = false) }
  }

  fun updateSong() {
    val songData = createSongData()
    val validationResult = songData.validate()
    if (validationResult != NewSongResult.Valid) {
      onSongDataValidationResult(validationResult)
    } else {
      performSongUpdate(songData)
    }
  }

  private fun performSongUpdate(songData: SongData) {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      val result = withContext(dispatchers.background) {
        songsRepository.updateSong(args.songId, songData)
      }
      onSongUpdateResult(result)
    }
  }

  private fun onSongDataValidationResult(result: NewSongResult) {
    when (result) {
      NewSongResult.EmptyTitle -> onEmptyTitleError()
      NewSongResult.EmptyPerformer -> onEmptyPerformerError()
      NewSongResult.EmptyLyrics -> onEmptyLyricsError()
      else -> {}
    }
  }

  private fun onSongUpdateResult(result: SongResult) {
    when (result) {
      SongResult.Updated -> onSongUpdated()
      SongResult.ErrorUpdating -> onErrorUpdatingSong()
      else -> {}
    }
  }

  private fun onErrorUpdatingSong() {
    _uiState.update { it.copy(isLoading = false, isErrorSavingSong = true) }
  }

  private fun onSongUpdated() {
    _uiState.update { it.copy(isLoading = false, songUpdated = true) }
  }

  private fun onEmptyLyricsError() {
    _uiState.update { it.copy(isLoading = false, isEmptyLyricsError = true) }
  }

  private fun onEmptyPerformerError() {
    _uiState.update { it.copy(isLoading = false, isEmptyPerformerNameError = true) }
  }

  private fun onEmptyTitleError() {
    _uiState.update { it.copy(isLoading = false, isEmptySongTitleError = true) }
  }

  private fun createSongData(): SongData = SongData(
    SongTitle(uiState.value.songTitle),
    SongPerformer(uiState.value.songPerformer),
    SongLyrics(uiState.value.songLyrics)
  )
}
