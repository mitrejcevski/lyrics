package nl.jovmit.lyrics.main.overview

import nl.jovmit.lyrics.main.SongsService
import nl.jovmit.lyrics.main.data.result.SongResult
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

    suspend fun findSongById(songId: String): SongResult {
        return try {
            val song = songsService.findSongById(songId)
            SongResult.Fetched(song)
        } catch (serviceException: SongsServiceException) {
            SongResult.NotFound
        }
    }

    suspend fun searchSongs(query: String): SongsResult {
        return try {
            val songs = songsService.search(query)
            return SongsResult.Fetched(songs)
        } catch (serviceException: SongsServiceException) {
            SongsResult.SearchError
        }
    }

    suspend fun deleteSongById(songId: String) {
        TODO("not implemented")
    }
}
