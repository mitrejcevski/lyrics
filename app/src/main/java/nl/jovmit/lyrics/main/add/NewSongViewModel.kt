package nl.jovmit.lyrics.main.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.overview.SongsRepository

class NewSongViewModel(
  private val songsRepository: SongsRepository,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  private val newSongLiveData = MutableLiveData<NewSongResult>()

  fun newSongLiveData(): LiveData<NewSongResult> = newSongLiveData

  private val _uiState = MutableStateFlow(NewSongScreenState())
  val uiState: StateFlow<NewSongScreenState> = _uiState

  fun updateSongTitle(newValue: String) {
    _uiState.update { it.copy(songTitle = newValue, isEmptySongTitleError = false) }
  }

  fun updatePerformerName(newValue: String) {
    _uiState.update { it.copy(performerName = newValue, isEmptyPerformerNameError = false) }
  }

  fun updateLyrics(newValue: String) {
    _uiState.update { it.copy(lyrics = newValue, isEmptyLyricsError = false) }
  }

  fun dismissAddSongError() {
    _uiState.update { it.copy(errorAddingSong = false) }
  }

  fun saveSong() {
    addNewSong(
      title = uiState.value.songTitle,
      performer = uiState.value.performerName,
      lyrics = uiState.value.lyrics
    )
  }

  fun addNewSong(title: String, performer: String, lyrics: String) {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      newSongLiveData.value = NewSongResult.Loading(true)
      val result = withContext(dispatchers.background) {
        val newSong = createSongFor(title, performer, lyrics)
        submitSongToRepository(newSong)
      }
      updateUiStateFor(result)
      newSongLiveData.value = result
      newSongLiveData.value = NewSongResult.Loading(false)
    }
  }

  private fun updateUiStateFor(result: NewSongResult) {
    when (result) {
      is NewSongResult.SongAdded -> onSongAdded()
      is NewSongResult.ErrorAddingSong -> onErrorAddingSong()
      is NewSongResult.EmptyTitle -> onEmptySongTitle()
      is NewSongResult.EmptyPerformer -> onEmptyPerformerError()
      is NewSongResult.EmptyLyrics -> onEmptyLyricsError()
      else -> {}
    }
  }

  private fun onEmptyLyricsError() {
    _uiState.update { it.copy(isLoading = false, isEmptyLyricsError = true) }
  }

  private fun onEmptyPerformerError() {
    _uiState.update { it.copy(isLoading = false, isEmptyPerformerNameError = true) }
  }

  private fun onEmptySongTitle() {
    _uiState.update { it.copy(isLoading = false, isEmptySongTitleError = true) }
  }

  private fun onErrorAddingSong() {
    _uiState.update { it.copy(isLoading = false, errorAddingSong = true) }
  }

  private fun onSongAdded() {
    _uiState.update { it.copy(isLoading = false, songAdded = true) }
  }

  private suspend fun submitSongToRepository(newSongData: SongData): NewSongResult {
    val validationResult = newSongData.validate()
    return if (validationResult == NewSongResult.Valid) {
      songsRepository.addNewSong(newSongData)
    } else {
      validationResult
    }
  }

  private fun createSongFor(title: String, performer: String, lyrics: String): SongData {
    return SongData(
      SongTitle(title),
      SongPerformer(performer),
      SongLyrics(lyrics)
    )
  }
}
