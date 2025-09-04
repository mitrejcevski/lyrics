package nl.jovmit.lyrics.main.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.data.result.SongsResult

class SongsViewModel(
  private val songsRepository: SongsRepository,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  private val _uiState = MutableStateFlow(SongsOverviewScreenState())
  val uiState: StateFlow<SongsOverviewScreenState> = _uiState

  fun updateSearchQuery(newQuery: String) {
    _uiState.update { it.copy(searchQuery = newQuery) }
  }

  fun dismissSongsLoadingError() {
    _uiState.update { it.copy(isErrorLoadingSongs = false) }
  }

  fun dismissSongsSearchingError() {
    _uiState.update { it.copy(isErrorSearching = false) }
  }

  fun fetchSongs() {
    _uiState.update { it.copy(isLoading = true) }
    viewModelScope.launch {
      val songsResult = withContext(dispatchers.background) {
        songsRepository.fetchAllSongs()
      }
      onSongsResult(songsResult)
    }
  }

  fun performSearch() {
    _uiState.update { it.copy(isLoading = true) }
    viewModelScope.launch {
      val result = withContext(dispatchers.background) {
        songsRepository.searchSongs(uiState.value.searchQuery)
      }
      onSongsResult(result)
    }
  }

  private fun onSongsResult(result: SongsResult) {
    when (result) {
      is SongsResult.Fetched -> onSongsLoaded(result)
      is SongsResult.FetchingError -> onErrorLoadingSongs()
      is SongsResult.SearchError -> onErrorSearchingSongs()
    }
  }

  private fun onSongsLoaded(result: SongsResult.Fetched) {
    _uiState.update { it.copy(isLoading = false, songs = result.songs) }
  }

  private fun onErrorSearchingSongs() {
    _uiState.update { it.copy(isLoading = false, isErrorSearching = true) }
  }

  private fun onErrorLoadingSongs() {
    _uiState.update { it.copy(isLoading = false, isErrorLoadingSongs = true) }
  }
}
