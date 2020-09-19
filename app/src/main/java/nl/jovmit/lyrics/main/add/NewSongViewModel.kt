package nl.jovmit.lyrics.main.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle
import nl.jovmit.lyrics.main.overview.SongsRepository

class NewSongViewModel(
    private val songsRepository: SongsRepository,
    private val dispatchers: CoroutineDispatchers
) : CoroutineViewModel(dispatchers) {

    private val newSongLiveData = MutableLiveData<NewSongResult>()

    fun newSongLiveData(): LiveData<NewSongResult> = newSongLiveData

    fun addNewSong(title: String, performer: String, lyrics: String) {
        launch {
            newSongLiveData.value = NewSongResult.Loading(true)
            val result = withContext(dispatchers.background) {
                val newSong = createSongFor(title, performer, lyrics)
                submitSongToRepository(newSong)
            }
            newSongLiveData.value = result
            newSongLiveData.value = NewSongResult.Loading(false)
        }
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
