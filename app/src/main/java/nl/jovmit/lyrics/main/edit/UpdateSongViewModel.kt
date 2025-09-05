package nl.jovmit.lyrics.main.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
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
) : CoroutineViewModel(dispatchers) {

  private val args = savedStateHandle.toEditSongArgs()

  private val _uiState = MutableStateFlow(EditSongScreenState())
  val uiState: StateFlow<EditSongScreenState> = _uiState

  private val updateLiveData = MutableLiveData<SongResult>()
  fun updateSongLiveData(): LiveData<SongResult> = updateLiveData

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
    val state = uiState.value
    updateSong(
      args.songId,
      SongTitle(state.songTitle),
      SongPerformer(state.songPerformer),
      SongLyrics(state.songLyrics)
    )
  }

  fun updateSong(
    songId: String,
    songTitle: SongTitle,
    songPerformer: SongPerformer,
    songLyrics: SongLyrics
  ) {
    _uiState.update { it.copy(isLoading = true) }
    launch {
      updateLiveData.value = SongResult.Loading(true)
      val result = withContext(dispatchers.background) {
        val songData = SongData(songTitle, songPerformer, songLyrics)
        val validationResult = songData.validate()
        onSongDataValidationResult(validationResult)
        if (validationResult != NewSongResult.Valid) {
          SongResult.NotFound
        } else {
          songsRepository.updateSong(songId, songData)
        }
      }
      onSongUpdateResult(result)
      updateLiveData.value = result
      updateLiveData.value = SongResult.Loading(false)
    }
  }

  private fun onSongDataValidationResult(result: NewSongResult) {
    when (result) {
      NewSongResult.EmptyTitle -> _uiState.update { it.copy(isLoading = false, isEmptySongTitleError = true) }
      NewSongResult.EmptyPerformer -> _uiState.update { it.copy(isLoading = false, isEmptyPerformerNameError = true) }
      NewSongResult.EmptyLyrics -> _uiState.update { it.copy(isLoading = false, isEmptyLyricsError = true) }
      else -> {}
    }
  }

  private fun onSongUpdateResult(result: SongResult) {
    when (result) {
      SongResult.Updated -> _uiState.update { it.copy(isLoading = false, songUpdated = true) }
      SongResult.ErrorUpdating -> _uiState.update { it.copy(isLoading = false, isErrorSavingSong = true) }
      else -> {}
    }
  }
}
