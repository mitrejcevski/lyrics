package nl.jovmit.lyrics.main.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineViewModel
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongLyrics
import nl.jovmit.lyrics.main.data.song.SongPerformer
import nl.jovmit.lyrics.main.data.song.SongTitle

class NewSongViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val newSongRepository: NewSongRepository
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

    private suspend fun submitSongToRepository(newSong: Song): NewSongResult {
        val validationResult = newSong.validate()
        return if (validationResult == NewSongResult.Valid) {
            newSongRepository.addNewSong(newSong)
        } else {
            validationResult
        }
    }

    private fun createSongFor(title: String, performer: String, lyrics: String): Song {
        return Song(
            SongTitle(title),
            SongPerformer(performer),
            SongLyrics(lyrics)
        )
    }
}
