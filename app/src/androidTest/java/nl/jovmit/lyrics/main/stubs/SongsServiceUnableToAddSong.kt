package nl.jovmit.lyrics.main.stubs

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.exceptions.SongsServiceException

class SongsServiceUnableToAddSong : StubSongsService() {

    override suspend fun fetchAllSongs(): List<Song> {
        return emptyList()
    }

    override suspend fun addNewSong(newSongData: SongData) {
        throw SongsServiceException()
    }
}
