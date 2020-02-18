package nl.jovmit.lyrics.main.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.data.result.SongResult
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.overview.SongsRepository

class UpdateSongViewModel(
    private val songsRepository: SongsRepository,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val updateLiveData = MutableLiveData<SongResult>()

    fun updateSongLiveData(): LiveData<SongResult> = updateLiveData

    fun updateSong(
        songId: String,
        songTitle: SongTitle,
        songPerformer: SongPerformer,
        songLyrics: SongLyrics
    ) {
        launch {
            updateLiveData.value = SongResult.Loading(true)
            val result = withContext(dispatchers.background) {
                val songData = SongData(songTitle, songPerformer, songLyrics)
                songsRepository.updateSong(songId, songData)
            }
            updateLiveData.value = result
            updateLiveData.value = SongResult.Loading(false)
        }
    }
}
