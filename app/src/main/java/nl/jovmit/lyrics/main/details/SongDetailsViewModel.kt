package nl.jovmit.lyrics.main.details

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
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.overview.SongsRepository

private fun SavedStateHandle.toSongDetailsNavArgs(): SongDetailsArgs {
    return SongDetailsArgs(get("songId") ?: "")
}

class SongDetailsViewModel(
    private val songsRepository: SongsRepository,
    private val dispatchers: CoroutineDispatchers,
    savedStateHandle: SavedStateHandle,
) : CoroutineViewModel(dispatchers) {

    private val args = savedStateHandle.toSongDetailsNavArgs()

    private val _uiState = MutableStateFlow(SongDetailsScreenState())
    val uiState: StateFlow<SongDetailsScreenState> = _uiState

    private val songDetailsLiveData = MutableLiveData<SongResult>()
    fun songDetailsLiveData(): LiveData<SongResult> = songDetailsLiveData

    fun dismissSongNotFoundError() {
        _uiState.update { it.copy(isSongNotFoundError = false) }
    }

    fun dismissSongDeletingError() {
        _uiState.update { it.copy(isSongDeletingError = false) }
    }

    fun loadSong() {
        fetchSongById(args.songId)
    }

    fun fetchSongById(songId: String) {
        songDetailsLiveData.value = SongResult.Loading(true)
        _uiState.update { it.copy(isLoading = true) }
        launch {
            val result = withContext(dispatchers.background) {
                songsRepository.findSongById(songId)
            }
            onSongResult(result)
            songDetailsLiveData.value = result
            songDetailsLiveData.value = SongResult.Loading(false)
        }
    }

    fun deleteSong() {
        deleteSongById(args.songId)
    }

    fun deleteSongById(songId: String) {
        songDetailsLiveData.value = SongResult.Loading(true)
        _uiState.update { it.copy(isLoading = true) }
        launch {
            val result = withContext(dispatchers.background) {
                songsRepository.deleteSongById(songId)
            }
            onSongResult(result)
            songDetailsLiveData.value = result
            songDetailsLiveData.value = SongResult.Loading(false)
        }
    }

    private fun onSongResult(result: SongResult) {
        when (result) {
            is SongResult.Fetched -> _uiState.update { it.copy(isLoading = false, song = result.song) }
            is SongResult.NotFound -> _uiState.update { it.copy(isLoading = false, isSongNotFoundError = true) }
            is SongResult.Deleted -> _uiState.update { it.copy(isLoading = false, isSongDeleted = true) }
            is SongResult.ErrorDeleting -> _uiState.update { it.copy(isLoading = false, isSongDeletingError = true) }
            else -> {}
        }
    }
}
