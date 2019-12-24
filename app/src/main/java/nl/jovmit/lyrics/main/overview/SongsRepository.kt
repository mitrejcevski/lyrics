package nl.jovmit.lyrics.main.overview

import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.SongsResult
import nl.jovmit.lyrics.main.exceptions.SongsServiceException

class SongsRepository(private val songsService: SongsService) {

    suspend fun fetchAllSongs(): SongsResult {
        return try {
            val songs = songsService.fetchAllSongs()
            SongsResult.Fetched(songs)
        } catch (serviceError: SongsServiceException) {
            SongsResult.FetchingError
        }
    }
}
