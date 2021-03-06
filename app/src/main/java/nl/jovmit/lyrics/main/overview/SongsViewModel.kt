package nl.jovmit.lyrics.main.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.data.result.SongsResult

class SongsViewModel(
    private val songsRepository: SongsRepository,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val songsLiveData = MutableLiveData<SongsResult>()

    fun songsLiveData(): LiveData<SongsResult> = songsLiveData

    fun fetchSongs() {
        songsLiveData.value = SongsResult.Loading(true)
        launch {
            val songsResult = withContext(dispatchers.background) {
                songsRepository.fetchAllSongs()
            }
            songsLiveData.value = songsResult
            songsLiveData.value = SongsResult.Loading(false)
        }
    }

    fun search(query: String) {
        songsLiveData.value = SongsResult.Loading(true)
        launch {
            val result = withContext(dispatchers.background) {
                songsRepository.searchSongs(query)
            }
            songsLiveData.value = result
            songsLiveData.value = SongsResult.Loading(false)
        }
    }
}
