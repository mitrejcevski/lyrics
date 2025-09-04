package nl.jovmit.lyrics.main.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.data.result.SongsResult

class SongsViewModel(
    private val songsRepository: SongsRepository,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val _uiState = MutableStateFlow(SongsOverviewScreenState())
    val uiState: StateFlow<SongsOverviewScreenState> = _uiState

    private val songsLiveData = MutableLiveData<SongsResult>()
    fun songsLiveData(): LiveData<SongsResult> = songsLiveData

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
        songsLiveData.value = SongsResult.Loading(true)
        _uiState.update { it.copy(isLoading = true) }
        launch {
            val songsResult = withContext(dispatchers.background) {
                songsRepository.fetchAllSongs()
            }
            onSongsResult(songsResult)
            songsLiveData.value = songsResult
            songsLiveData.value = SongsResult.Loading(false)
        }
    }

    fun performSearch() {
        search(uiState.value.searchQuery)
    }

    fun search(query: String) {
        songsLiveData.value = SongsResult.Loading(true)
        _uiState.update { it.copy(isLoading = true) }
        launch {
            val result = withContext(dispatchers.background) {
                songsRepository.searchSongs(query)
            }
            onSongsResult(result)
            songsLiveData.value = result
            songsLiveData.value = SongsResult.Loading(false)
        }
    }

    private fun onSongsResult(result: SongsResult) {
        when (result) {
            is SongsResult.Fetched -> _uiState.update { it.copy(isLoading = false, songs = result.songs) }
            is SongsResult.FetchingError -> _uiState.update { it.copy(isLoading = false, isErrorLoadingSongs = true) }
            is SongsResult.SearchError -> _uiState.update { it.copy(isLoading = false, isErrorSearching = true) }
            else -> {}
        }
    }
}
