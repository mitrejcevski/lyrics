package nl.jovmit.lyrics.main.stubs

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.exceptions.SongsServiceException

class SongsServiceUnableToFetchSongs : StubSongsService() {

    override suspend fun fetchAllSongs(): List<Song> {
        throw SongsServiceException()
    }
}
