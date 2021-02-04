package nl.jovmit.lyrics.main

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.exceptions.SongsServiceException

class UnavailableSongService(
    private val songsList: List<Song>? = emptyList()
) : SongsService {

    override suspend fun fetchAllSongs(): List<Song> {
        return songsList ?: throw SongsServiceException()
    }

    override suspend fun addNewSong(newSongData: SongData) {
        throw SongsServiceException()
    }

    override suspend fun findSongById(songId: String): Song {
        return songsList
            ?.first { it.songId.value == songId }
            ?: throw SongsServiceException()
    }

    override suspend fun search(query: String): List<Song> {
        throw SongsServiceException()
    }

    override suspend fun deleteSongById(songId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSong(songId: String, songData: SongData) {
        throw SongsServiceException()
    }
}