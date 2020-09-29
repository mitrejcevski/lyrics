package nl.jovmit.lyrics.main.stubs

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.exceptions.SongsServiceException

class SongsServiceUnableToSearchSongs(
    private val songsList: List<Song> = emptyList()
) : StubSongsService() {

    override suspend fun fetchAllSongs(): List<Song> {
        return songsList
    }

    override suspend fun search(query: String): List<Song> {
        throw SongsServiceException()
    }
}
