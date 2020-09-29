package nl.jovmit.lyrics.main.stubs

import nl.jovmit.lyrics.main.data.song.Song
import nl.jovmit.lyrics.main.data.song.SongData
import nl.jovmit.lyrics.main.exceptions.SongsServiceException

class SongsServiceUnableToUpdateSong(
    private val songsList: List<Song> = emptyList()
) : StubSongsService() {

    override suspend fun fetchAllSongs(): List<Song> {
        return songsList
    }

    override suspend fun findSongById(songId: String): Song {
        return songsList.first { it.songId.value == songId }
    }

    override suspend fun updateSong(songId: String, songData: SongData) {
        throw SongsServiceException()
    }
}
