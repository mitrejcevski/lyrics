package nl.jovmit.lyrics.main.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.overview.SongsRepository

class SongDetailsViewModel(
    private val songsRepository: SongsRepository,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val songDetailsLiveData = MutableLiveData<SongResult>()

    fun songDetailsLiveData(): LiveData<SongResult> = songDetailsLiveData

    fun fetchSongById(songId: String) {
        launch {
            songDetailsLiveData.value = SongResult.Loading(true)
            val result = withContext(dispatchers.background) {
                songsRepository.findSongById(songId)
            }
            songDetailsLiveData.value = result
            songDetailsLiveData.value = SongResult.Loading(false)
        }
    }

    fun deleteSongById(songId: String) {

    }
}
