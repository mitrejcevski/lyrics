package nl.jovmit.lyrics.main.add

import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.exceptions.SongsServiceException

class NewSongRepository(
    private val songsService: SongsService
) {

    suspend fun addNewSong(newSongData: SongData): NewSongResult {
        return try {
            songsService.addNewSong(newSongData)
            NewSongResult.SongAdded
        } catch (serviceException: SongsServiceException) {
            NewSongResult.ErrorAddingSong
        }
    }
}
