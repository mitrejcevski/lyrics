package nl.jovmit.lyrics.main.add

import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.NewSongResult
import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.exceptions.SongsServiceException

class NewSongRepository(
    private val songsService: SongsService
) {

    suspend fun addNewSong(newSong: Song): NewSongResult {
        return try {
            songsService.addNewSong(newSong)
            NewSongResult.SongAdded
        } catch (serviceException: SongsServiceException) {
            NewSongResult.ErrorAddingSong
        }
    }
}
